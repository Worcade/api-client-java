// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.internal;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.net.HttpHeaders;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.worcade.client.Result;
import net.worcade.client.Worcade;
import net.worcade.client.api.ApplicationApi;
import net.worcade.client.api.AssetApi;
import net.worcade.client.api.AttachmentApi;
import net.worcade.client.api.ChecklistApi;
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
import net.worcade.client.get.Authentication;
import net.worcade.client.get.BinaryData;
import net.worcade.client.get.ExternalNumber;
import net.worcade.client.get.Notification;
import net.worcade.client.get.OptionalField;
import net.worcade.client.get.Reference;
import net.worcade.client.get.ReferenceWithName;
import net.worcade.client.get.RemoteId;
import net.worcade.client.get.Webhook;
import net.worcade.client.get.WorkOrder;

import java.io.InputStream;
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
    protected static String PUBLIC_API = "api/v" + Worcade.VERSION.getMajor() + "/";

    protected static final Function<Object, IncomingDto> DTO_FUNCTION = o -> {
        @SuppressWarnings("unchecked") Map<String, Object> data = (Map<String, Object>) o;
        return IncomingDto.of(data);
    };

    @Getter(AccessLevel.PROTECTED) private final String baseUrl;

    @Getter(AccessLevel.PROTECTED) @Setter(AccessLevel.PROTECTED) private String userHeader;
    @Getter(AccessLevel.PROTECTED) @Setter(AccessLevel.PROTECTED) private String applicationHeader;

    @Getter private final ApplicationApi applicationApi = new WorcadeApi(this, PUBLIC_API + "application");
    @Getter private final AssetApi assetApi = new WorcadeApi(this, PUBLIC_API + "asset");
    @Getter private final AttachmentApi attachmentApi = new WorcadeApi(this, PUBLIC_API + "attachment");
    @Getter private final ChecklistApi checklistApi = new WorcadeApi(this, PUBLIC_API + "checklist");
    @Getter private final CompanyApi companyApi = new WorcadeApi(this, PUBLIC_API + "company");
    @Getter private final ContactsApi contactsApi = new WorcadeApi(this, PUBLIC_API + "contacts");
    @Getter private final ConversationApi conversationApi = new WorcadeApi(this, PUBLIC_API + "conversation");
    @Getter private final GroupApi groupApi = new WorcadeApi(this, PUBLIC_API + "group");
    @Getter private final LabelApi labelApi = new WorcadeApi(this, PUBLIC_API + "label");
    @Getter private final ReclaimApi reclaimApi = new WorcadeApi(this, PUBLIC_API + "reclaim");
    @Getter private final RoomApi roomApi = new WorcadeApi(this, PUBLIC_API + "room");
    @Getter private final SearchApi searchApi  = new WorcadeApi(this, PUBLIC_API + "search");
    @Getter private final SiteApi siteApi = new WorcadeApi(this, PUBLIC_API + "site");
    @Getter private final UserApi userApi = new WorcadeApi(this, PUBLIC_API + "user");
    @Getter private final WebhookApi webhookApi = new WorcadeApi(this, PUBLIC_API + "webhook");
    @Getter private final WorkOrderApi workOrderApi = new WorcadeApi(this, PUBLIC_API + "workorder");

    protected WorcadeClient(String baseUrl) {
        checkArgument(baseUrl.startsWith("http"), "Base URL must include protocol https:// or http://");

        this.baseUrl = (baseUrl.endsWith("/") ? baseUrl : baseUrl + "/");
    }

    protected Result<? extends Worcade> checkServerVersion() {
        Result<IncomingDto> result = get(PUBLIC_API + "about/version");
        if (result.isOk()) {
            IncomingDto serverVersion = result.getResult();
            if (serverVersion.getInt("major") != Worcade.VERSION.getMajor()) {
                throw new IncompatibleVersionException(serverVersion.getString("version"));
            }
            if (serverVersion.getInt("minor") < Worcade.VERSION.getMinor()) {
                throw new IncompatibleVersionException(serverVersion.getString("version"));
            }
            if (serverVersion.getInt("minor") == Worcade.VERSION.getMinor() && serverVersion.getInt("patch") < Worcade.VERSION.getPatch()) {
                throw new IncompatibleVersionException(serverVersion.getString("version"));
            }
            log.info("Worcade {} initialized on {}, running version {}", getClass().getSimpleName(), baseUrl, serverVersion.getString("version"));
        }
        else {
            log.warn("Version check failed with {}; not throwing to allow reconnect", result.getMessages());
        }
        return result.map(v -> this);
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
        Result<IncomingDto> result = get(PUBLIC_API + "authentication/user" + urlSuffix,
                new Header(HttpHeaders.AUTHORIZATION, "BASIC " + Util.encodeBase64(authHeaderValue)));
        if (result.isOk()) {
            setUserToken(result.getResult().getString("token"));
        }
        return result.map(d -> d.getDto("authenticated"));
    }

    @Override
    public Result<ReferenceWithName> setUserApiKey(String apiKey) {
        userHeader = "APIKEY " + apiKey;
        applicationHeader = null;
        Result<ReferenceWithName> result = getAuthentication().map(Authentication::getUser);
        if (!result.isOk()) {
            userHeader = null;
        }
        return result;
    }

    @Override
    public Result<?> logoutUser() {
        if (userHeader == null || !userHeader.startsWith("DIGEST ")) {
            userHeader = null;
            return Result.ok(null);
        }
        Result<IncomingDto> result = delete(PUBLIC_API + "authentication/user");
        userHeader = null;
        return result;
    }

    @Override
    public Result<ReferenceWithName> loginApplication(String applicationId, PrivateKey applicationPrivateKey, PublicKey worcadePublicKey) {
        userHeader = null;
        applicationHeader = null;
        checkId(applicationId);

        Result<IncomingDto> result = get(PUBLIC_API + "authentication/application/" + applicationId + "/handshake")
                .flatMap(d -> Util.decodeAndDecrypt((d.getString("digest")), applicationPrivateKey))
                .flatMap(d -> Util.encryptAndEncode(d, worcadePublicKey))
                .flatMap(d -> post(PUBLIC_API + "authentication/application/" + applicationId + "/session", ImmutableMap.of("digest", d)));

        if (result.isOk()) {
            setApplicationToken(result.getResult().getString("token"));
        }
        return result.map(d -> d.getDto("authenticated"));
    }

    @Override
    public Result<ReferenceWithName> setApplicationSourceAuth(String id) {
        userHeader = null;
        applicationHeader = id;
        return id == null ? Result.ok(null) : getAuthentication().map(Authentication::getApplication);
    }

    @Override
    public Result<ReferenceWithName> setApplicationApiKey(String apiKey) {
        userHeader = null;
        applicationHeader = "APIKEY " + apiKey;
        Result<ReferenceWithName> result = getAuthentication().map(Authentication::getApplication);
        if (!result.isOk()) {
            applicationHeader = null;
        }
        return result;
    }

    @Override
    public Result<? extends Authentication> setTrustedUser(Reference user) {
        checkState(applicationHeader != null, "No authenticated application");
        if (user == null) {
            userHeader = null;
            return Result.ok(null);
        }
        Result<IncomingDto> result = get(PUBLIC_API + "authentication", new Header("Worcade-User", checkId(user.getId())));
        if (result.isOk()) {
            log.trace("Authentication with trusted user: {}", result.getResult());
            userHeader = user.getId();
        }
        return result;
    }

    @Override
    public Result<?> logoutApplication() {
        if (applicationHeader == null || !applicationHeader.startsWith("DIGEST")) {
            applicationHeader = null;
            userHeader = null;
            return Result.ok(null);
        }
        Result<IncomingDto> result = delete(PUBLIC_API + "authentication/application");
        applicationHeader = null;
        userHeader = null;
        return result;
    }

    @Override
    public Result<?> invalidateCurrentApiKey() {
        if (applicationHeader != null && applicationHeader.startsWith("APIKEY")) {
            Result<IncomingDto> result = getAuthentication().flatMap(auth -> delete(PUBLIC_API + "application/" + auth.getApplication().getId() + "/apikey"));
            applicationHeader = null;
            userHeader = null;
            return result;
        }
        if (userHeader != null && userHeader.startsWith("APIKEY")) {
            Result<IncomingDto> result = getAuthentication().flatMap(auth -> delete(PUBLIC_API + "user/" + auth.getUser().getId() + "/apikey"));
            applicationHeader = null;
            userHeader = null;
            return result;
        }
        return Result.failed(ImmutableList.of(new Result.Message(null, "No active API key")));
    }

    @VisibleForTesting
    public Result<? extends Authentication> getSecretAuthentication(String userId, String secret) {
        return get(PUBLIC_API + "authentication", new Header("Worcade-User", userId), new Header("Worcade-Secret", secret));
    }

    @Override
    public Result<? extends Authentication> getAuthentication() {
        return get(PUBLIC_API + "authentication");
    }

    @Override
    public Result<Boolean> probeUserTrust(String userId, String applicationId) {
        return get(PUBLIC_API + "authentication/user/" + checkId(userId) + "/trusted/" + checkId(applicationId)).map(d -> d.getBoolean("trusted"));
    }

    @Override
    public Result<? extends Collection<? extends Notification>> getNotifications() {
        return getList(PUBLIC_API + "notification");
    }

    @Override
    public RemoteId createRemoteId(String remoteIdType, String remoteId) {
        return IncomingDto.of(ImmutableMap.of("remoteIdType", remoteIdType, "remoteId", remoteId));
    }

    @Override
    public OptionalField createOptionalField(String name, String value, Reference owner) {
        if (owner == null) {
            return IncomingDto.of(ImmutableMap.of("name", name, "value", value));
        }
        return IncomingDto.of(ImmutableMap.of("name", name, "value", value, "owner", Modification.cleanReference(owner)));
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

    @CanIgnoreReturnValue
    static String checkId(String id) {
        try {
            //noinspection ResultOfMethodCallIgnored
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

    @Override
    public WorcadeClient copyWithSameAuth() {
        WorcadeClient client = copy();
        client.applicationHeader = applicationHeader;
        client.userHeader = userHeader;
        return client;
    }
    protected abstract WorcadeClient copy();

    protected abstract Result<IncomingDto> get(String url, Header... additionalHeader);
    protected abstract Result<List<IncomingDto>> getList(String url, Header... additionalHeader);
    protected abstract Result<BinaryData> getBinary(String url, Header... additionalHeader);
    protected abstract Result<IncomingDto> post(String url, Object data, Header... additionalHeader);
    protected abstract Result<IncomingDto> postBinary(String url, InputStream data, String contentType, Header... additionalHeader);
    protected abstract Result<IncomingDto> put(String url, Object data, Header... additionalHeader);
    protected abstract Result<IncomingDto> delete(String url, Header... additionalHeader);
    protected abstract Result<IncomingDto> delete(String url, Object data, Header... additionalHeader);
}
