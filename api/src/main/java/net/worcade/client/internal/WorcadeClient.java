// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.internal;

import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.worcade.client.Result;
import net.worcade.client.Worcade;
import net.worcade.client.api.ApplicationApi;
import net.worcade.client.api.AssetApi;
import net.worcade.client.api.AttachmentApi;
import net.worcade.client.api.CompanyApi;
import net.worcade.client.api.ContactsApi;
import net.worcade.client.api.ConversationApi;
import net.worcade.client.api.GroupApi;
import net.worcade.client.api.LabelApi;
import net.worcade.client.api.ReclaimApi;
import net.worcade.client.api.RoomApi;
import net.worcade.client.api.SearchApi;
import net.worcade.client.api.SiteApi;
import net.worcade.client.api.UserApi;
import net.worcade.client.api.WebhookApi;
import net.worcade.client.api.WorkOrderApi;
import net.worcade.client.exception.IncompatibleVersionException;
import net.worcade.client.exception.InvalidIdException;
import net.worcade.client.get.ExternalNumber;
import net.worcade.client.get.Notification;
import net.worcade.client.get.Reference;
import net.worcade.client.get.ReferenceWithName;
import net.worcade.client.get.RemoteId;
import net.worcade.client.get.Webhook;
import net.worcade.client.get.WorkOrder;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Duration;
import java.util.Collection;
import java.util.Currency;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import static com.google.common.base.Preconditions.*;

@Slf4j
public abstract class WorcadeClient implements Worcade {
    protected static final Function<Object, IncomingDto> DTO_FUNCTION = o -> {
        @SuppressWarnings("unchecked") Map<String, Object> data = (Map<String, Object>) o;
        return IncomingDto.of(data);
    };

    @Getter(AccessLevel.PROTECTED) private final String baseUrl;
    @Getter(AccessLevel.PROTECTED) private final String apiKey;

    @Getter(AccessLevel.PROTECTED) private String userHeader;
    @Getter(AccessLevel.PROTECTED) private String applicationHeader;

    @Getter private final ApplicationApi applicationApi = new WorcadeApi(this, "application");
    @Getter private final AssetApi assetApi = new WorcadeApi(this, "asset");
    @Getter private final AttachmentApi attachmentApi = new WorcadeApi(this, "attachment");
//    @Getter private final ChecklistApi checklistApi = new WorcadeApi(this, "checklist");
    @Getter private final WebhookApi webhookApi = new WorcadeApi(this, "webhook");
    @Getter private final CompanyApi companyApi = new WorcadeApi(this, "company");
    @Getter private final ContactsApi contactsApi = new WorcadeApi(this, "contacts");
    @Getter private final ConversationApi conversationApi = new WorcadeApi(this, "conversation");
    @Getter private final GroupApi groupApi = new WorcadeApi(this, "group");
    @Getter private final LabelApi labelApi = new WorcadeApi(this, "label");
    @Getter private final ReclaimApi reclaimApi = new WorcadeApi(this, "reclaim");
    @Getter private final RoomApi roomApi = new WorcadeApi(this, "room");
    @Getter private final SearchApi searchApi  = new WorcadeApi(this, "search");
    @Getter private final SiteApi siteApi = new WorcadeApi(this, "site");
    @Getter private final WorkOrderApi workOrderApi = new WorcadeApi(this, "workorder");

    protected WorcadeClient(String baseUrl, String apiKey) {
        checkArgument(baseUrl.startsWith("http"), "Base URL must include protocol https:// or http://");

        this.baseUrl = (baseUrl.endsWith("/") ? baseUrl : baseUrl + "/") + "api/v" + Worcade.VERSION.getMajor() + "/";
        this.apiKey = apiKey;
    }

    @Getter private final UserApi userApi = new WorcadeApi(this, "user");

    protected void checkServerVersion() {
        IncomingDto version = get("about/version").getResult();
        String serverVersion = version.getString("version");
        if (version.getInt("major") != Worcade.VERSION.getMajor()) {
            throw new IncompatibleVersionException(serverVersion);
        }
        if (version.getInt("minor") < Worcade.VERSION.getMinor()) {
            throw new IncompatibleVersionException(serverVersion);
        }
        if (version.getInt("minor") == Worcade.VERSION.getMinor() && version.getInt("patch") < Worcade.VERSION.getPatch()) {
            throw new IncompatibleVersionException(serverVersion);
        }
        log.info("Worcade {} initialized on {}, running version {}", getClass().getSimpleName(), baseUrl, serverVersion);
    }

    @Override
    public Result<ReferenceWithName> loginUserByEmail(String email, String password) {
        return loginUser(email + ":" + password, "/email");
    }

    @Override
    public Result<ReferenceWithName> loginUserById(String userId, String password) {
        checkId(userId);
        return loginUser(userId + ":" + password, "");
    }

    private Result<ReferenceWithName> loginUser(String authHeaderValue, String urlSuffix) {
        userHeader = null;
        applicationHeader = null;
        Result<IncomingDto> result = get("authentication/user" + urlSuffix,
                new Header(HttpHeaders.AUTHORIZATION, "BASIC " + Util.encodeBase64(authHeaderValue)));
        if (result.isOk()) {
            setUserToken(result.getResult().getString("token"));
        }
        return result.map(d -> d.getDto("authenticated"));
    }

    @Override
    public Result<?> logoutUser() {
        return delete("authentication/user");
    }

    @Override
    public Result<ReferenceWithName> loginApplication(String applicationId, PrivateKey applicationPrivateKey, PublicKey worcadePublicKey) {
        userHeader = null;
        applicationHeader = null;
        checkId(applicationId);

        Result<IncomingDto> result = get("authentication/application/" + applicationId + "/handshake")
                .flatMap(d -> Util.decodeAndDecrypt((d.getString("digest")), applicationPrivateKey))
                .flatMap(d -> Util.encryptAndEncode(d, worcadePublicKey))
                .flatMap(d -> post("authentication/application/" + applicationId + "/session", ImmutableMap.of("digest", d)));

        if (result.isOk()) {
            setApplicationToken(result.getResult().getString("token"));
        }
        return result.map(d -> d.getDto("authenticated"));
    }

    @Override
    public Result<?> setTrustedUser(Reference user) {
        checkState(applicationHeader != null, "No authenticated application");
        Result<IncomingDto> result = get("authentication", new Header("Worcade-User", checkId(user.getId())));
        if (result.isOk()) {
            log.trace("Authentication with trusted user: {}", result.getResult());
            userHeader = user.getId();
        }
        return result.cast();
    }

    @Override
    public Result<Boolean> probeUserTrust(String userId, String applicationId) {
        return get("authentication/user/" + checkId(userId) + "/trusted/" + checkId(applicationId)).map(d -> d.getBoolean("trusted"));
    }

    @Override
    public RemoteId createRemoteId(String remoteIdType, String remoteId) {
        return IncomingDto.of(ImmutableMap.of("remoteIdType", remoteIdType, "remoteId", remoteId));
    }

    @Override
    public ExternalNumber createExternalNumber(String number, String description) {
        return IncomingDto.of(ImmutableMap.of("number", number, "description", description));
    }

    @Override
    public Webhook.Header createWebhookHeader(String name, String value) {
        return IncomingDto.of(ImmutableMap.of("name", name, "value", value));
    }

    @Override
    public WorkOrder.Row createWorkOrderRow(String description, Duration duration, Double cost, Currency currency) {
        ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
        if (duration != null) {
            builder.put("duration", duration.getSeconds());
        }
        if (cost != null) {
            builder.put("costAmount", cost).put("costCurrency", currency.getCurrencyCode());
        }
        return IncomingDto.of(builder.put("description", description).build());
    }

    @Override
    public Result<? extends Collection<? extends Notification>> getNotifications() {
        return getList("notification");
    }

    static String checkId(String id) {
        try {
            UUID.fromString(id);
            return id;
        }
        catch (NullPointerException | IllegalArgumentException e) {
            throw new InvalidIdException(id);
        }
    }

    private void setUserToken(String token) {
        applicationHeader = null;
        userHeader = "DIGEST " + token;
    }

    private void setApplicationToken(String token) {
        userHeader = null;
        applicationHeader = "DIGEST " + token;
    }

    protected abstract Result<IncomingDto> get(String url, Header... additionalHeader);
    protected abstract Result<List<IncomingDto>> getList(String url, Header... additionalHeader);
    protected abstract Response getCustom(String url, Header... additionalHeader);
    protected abstract Result<IncomingDto> post(String url, Object data, Header... additionalHeader);
    protected abstract Response postCustom(String url, Entity<?> data, Header... additionalHeader);
    protected abstract Result<IncomingDto> put(String url, Object data, Header... additionalHeader);
    protected abstract Result<IncomingDto> delete(String url, Header... additionalHeader);
    protected abstract Result<IncomingDto> delete(String url, Object data, Header... additionalHeader);
    protected abstract Result<Object> handle(String method, String url, Response response);
}
