// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.internal;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import net.worcade.client.Result;
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
import net.worcade.client.create.WebhookCreate;
import net.worcade.client.create.WorkOrderCreate;
import net.worcade.client.get.Asset;
import net.worcade.client.get.AttachmentData;
import net.worcade.client.get.Contact;
import net.worcade.client.get.Conversation;
import net.worcade.client.get.ExternalNumber;
import net.worcade.client.get.Group;
import net.worcade.client.get.Reference;
import net.worcade.client.get.ReferenceWithName;
import net.worcade.client.get.ReferenceWithNumber;
import net.worcade.client.get.RemoteId;
import net.worcade.client.get.RemoteIdSearchResult;
import net.worcade.client.get.Room;
import net.worcade.client.get.Site;
import net.worcade.client.get.Webhook;
import net.worcade.client.get.WebhookTestResult;
import net.worcade.client.get.WorkOrder;
import net.worcade.client.modify.ApplicationModification;
import net.worcade.client.modify.AssetModification;
import net.worcade.client.modify.CompanyModification;
import net.worcade.client.modify.ConversationModification;
import net.worcade.client.modify.EntityModification;
import net.worcade.client.modify.GroupModification;
import net.worcade.client.modify.LabelModification;
import net.worcade.client.modify.RoomModification;
import net.worcade.client.modify.SiteModification;
import net.worcade.client.modify.UserModification;
import net.worcade.client.modify.WorkOrderModification;
import net.worcade.client.modify.WorkOrderRowModification;
import net.worcade.client.query.AssetField;
import net.worcade.client.query.ContactField;
import net.worcade.client.query.ConversationField;
import net.worcade.client.query.GroupField;
import net.worcade.client.query.LabelField;
import net.worcade.client.query.MemberField;
import net.worcade.client.query.Query;
import net.worcade.client.query.RoomField;
import net.worcade.client.query.SearchQuery;
import net.worcade.client.query.SiteField;
import net.worcade.client.query.WebhookField;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.*;

@Slf4j
class WorcadeApi implements ApplicationApi, AssetApi, AttachmentApi, CompanyApi, ContactsApi, ConversationApi, GroupApi,
        LabelApi, ReclaimApi, RoomApi, SearchApi, SiteApi, UserApi, WebhookApi, WorkOrderApi {
    private final WorcadeClient worcadeClient;
    private final String entityUrl;

    WorcadeApi(WorcadeClient worcadeClient, String entityUrl) {
        this.worcadeClient = worcadeClient;
        this.entityUrl = entityUrl;
    }

    public Modification createBuilder() {
        return Modification.create();
    }

    @Override
    public Result<IncomingDto> get(String id) {
        return worcadeClient.get(entityUrl + "/" + WorcadeClient.checkId(id));
    }

    @Override
    public Result<IncomingDto> getProfile(String id) {
        return worcadeClient.get(entityUrl + "/" + WorcadeClient.checkId(id) + "/profile");
    }

    @Override
    public Result<? extends Reference> create(ApplicationModification subject) {
        return createInternal(subject);
    }

    @Override
    public Result<? extends Reference> create(AssetModification subject) {
        return createInternal(subject);
    }

    @Override
    public Result<? extends Reference> create(CompanyModification subject) {
        return createInternal(subject);
    }

    @Override
    public Result<? extends ReferenceWithNumber> create(ConversationModification subject) {
        return createInternal(subject);
    }

    @Override
    public Result<? extends Reference> create(GroupModification subject) {
        return createInternal(subject);
    }


    @Override
    public Result<? extends Reference> create(LabelModification subject) {
        return createInternal(subject);
    }

    @Override
    public Result<? extends Reference> create(RoomModification subject) {
        return createInternal(subject);
    }

    @Override
    public Result<? extends Reference> create(SiteModification subject) {
        return createInternal(subject);
    }

    @Override
    public Result<? extends Reference> create(UserModification subject) {
        return createInternal(subject);
    }

    @Override
    public Result<? extends Reference> create(WebhookCreate subject) {
        return createInternal(subject);
    }

    @Override
    public Result<? extends Reference> create(String conversationId, WorkOrderCreate subject) {
        return worcadeClient.post("conversation/" + WorcadeClient.checkId(conversationId) + "/content/workorder",
                ((Modification) subject).getData());
    }

    @Override
    public Result<?> update(AssetModification subject) {
        return updateInternal(subject, "");
    }

    @Override
    public Result<?> update(CompanyModification subject) {
        return updateInternal(subject, "");
    }

    @Override
    public Result<?> update(ConversationModification subject) {
        return updateInternal(subject, "");
    }

    @Override
    public Result<?> update(LabelModification subject) {
        return updateInternal(subject, "");
    }

    @Override
    public Result<?> update(RoomModification subject) {
        return updateInternal(subject, "");
    }

    @Override
    public Result<?> update(SiteModification subject) {
        return updateInternal(subject, "");
    }

    @Override
    public Result<?> update(WorkOrderModification subject) {
        return updateInternal(subject, "");
    }

    @Override
    public Result<?> updateProfile(ApplicationModification subject) {
        return updateInternal(subject, "/profile");
    }

    @Override
    public Result<?> updateProfile(GroupModification subject) {
        return updateInternal(subject, "/profile");
    }

    @Override
    public Result<?> updateProfile(UserModification subject) {
        return updateInternal(subject, "/profile");
    }

    @Override
    public Result<?> delete(String id) {
        return worcadeClient.delete(entityUrl + "/" + WorcadeClient.checkId(id));
    }

    @Override
    public Result<? extends Collection<? extends Asset>> getAssetList(Query<AssetField> query) {
        return getList(query);
    }

    @Override
    public Result<? extends Collection<? extends Conversation>> getConversationList(Query<ConversationField> query) {
        return getList(query);
    }

    @Override
    public Result<? extends Collection<? extends Group>> getGroupList(Query<GroupField> query) {
        return getList(query);
    }

    @Override
    public Result<? extends Collection<? extends Room>> getLabelList(Query<LabelField> query) {
        return getList(query);
    }

    @Override
    public Result<? extends Collection<? extends Room>> getRoomList(Query<RoomField> query) {
        return getList(query);
    }

    @Override
    public Result<? extends Collection<? extends Site>> getSiteList(Query<SiteField> query) {
        return getList(query);
    }

    @Override
    public Result<?> addLabels(String id, Reference... labels) {
        return worcadeClient.post(entityUrl + "/" + WorcadeClient.checkId(id) + "/labels", Modification.cleanReferences(labels));
    }

    @Override
    public Result<?> removeLabels(String id, Reference... labels) {
        return worcadeClient.delete(entityUrl + "/" + WorcadeClient.checkId(id) + "/labels", Modification.cleanReferences(labels));
    }

    @Override
    public Result<?> addRemoteIds(String id, RemoteId... remoteIds) {
        return worcadeClient.post(entityUrl + "/" + WorcadeClient.checkId(id) + "/remoteIds", Modification.cleanRemoteIds(remoteIds));
    }

    @Override
    public Result<?> setRemoteIds(String id, RemoteId... remoteIds) {
        return worcadeClient.put(entityUrl + "/" + WorcadeClient.checkId(id) + "/remoteIds", Modification.cleanRemoteIds(remoteIds));
    }

    @Override
    public Result<?> removeRemoteIds(String id, RemoteId... remoteIds) {
        return worcadeClient.delete(entityUrl + "/" + WorcadeClient.checkId(id) + "/remoteIds", Modification.cleanRemoteIds(remoteIds));
    }

    @Override
    public Result<? extends Collection<? extends ReferenceWithName>> searchByEmail(String email) {
        return worcadeClient.getList(entityUrl + "/email?email=" + Util.escapeUrlQueryParameter(email));
    }

    @Override
    public Result<? extends Collection<? extends RemoteIdSearchResult>> searchByRemoteId(String remoteIdType, String remoteId) {
        if (Strings.isNullOrEmpty(remoteIdType)) {
            return worcadeClient.getList(entityUrl + "/remoteId");
        }
        if (Strings.isNullOrEmpty(remoteId)) {
            return worcadeClient.getList(entityUrl + "/remoteId?remoteIdType=" + Util.escapeUrlQueryParameter(remoteIdType));
        }
        return worcadeClient.getList(entityUrl + "/remoteId?remoteIdType=" + Util.escapeUrlQueryParameter(remoteIdType) + "&remoteId=" + Util.escapeUrlQueryParameter(remoteId));
    }

    @Override
    public Result<PublicKey> setupKeyExchange(String id, PublicKey applicationKey) {
        return Util.getPublicKeySpec(applicationKey)
                .flatMap(s -> worcadeClient.post(entityUrl + "/" + WorcadeClient.checkId(id) + "/authentication",
                        ImmutableMap.of("modulus", s.getModulus(), "exponent", s.getPublicExponent())))
                .flatMap(d -> Util.createPublicKey(new BigInteger(d.getString("modulus")), new BigInteger(d.getString("exponent"))));
    }

    @Override
    public Result<?> cancelAccount(String id, String password) {
        return worcadeClient.delete(entityUrl + "/" + WorcadeClient.checkId(id), new Header("Worcade-ConfirmPassword", Util.encodeBase64(password)));
    }

    @Override
    public Result<?> view(String id) {
        return worcadeClient.post(entityUrl + "/" + WorcadeClient.checkId(id) + "/view", null);
    }

    @Override
    public Result<?> addContent(String id, Reference... content) {
        return worcadeClient.post(entityUrl + "/" + WorcadeClient.checkId(id) + "/content", Modification.cleanReferences(content));
    }

    @Override
    public Result<?> addMessage(String id, String message) {
        return worcadeClient.post(entityUrl + "/" + WorcadeClient.checkId(id) + "/content/message", ImmutableMap.of("text", message));
    }

    @Override
    public Result<?> addEvaluation(String id, int rating) {
        return worcadeClient.post(entityUrl + "/" + WorcadeClient.checkId(id) + "/content/evaluation", ImmutableMap.of("rating", rating));
    }

    @Override
    public Result<?> addExternalNumbers(String id, ExternalNumber... numbers) {
        return worcadeClient.post(entityUrl + "/" + WorcadeClient.checkId(id) + "/externalNumbers", Modification.cleanExternalNumbers(numbers));
    }

    @Override
    public Result<?> addWatchers(String id, Reference... watchers) {
        return worcadeClient.post(entityUrl + "/" + WorcadeClient.checkId(id) + "/watchers", Modification.cleanReferences(watchers));
    }

    @Override
    public Result<?> removeWatchers(String id, Reference... watchers) {
        return worcadeClient.delete(entityUrl + "/" + WorcadeClient.checkId(id) + "/watchers", Modification.cleanReferences(watchers));
    }

    @Override
    public Result<?> addOwners(String id, Reference... owners) {
        return worcadeClient.post(entityUrl + "/" + WorcadeClient.checkId(id) + "/owners", Modification.cleanReferences(owners));
    }

    @Override
    public Result<?> setOwners(String id, Reference... owners) {
        return worcadeClient.put(entityUrl + "/" + WorcadeClient.checkId(id) + "/owners", Modification.cleanReferences(owners));
    }

    @Override
    public Result<?> removeOwners(String id, Reference... owners) {
        return worcadeClient.delete(entityUrl + "/" + WorcadeClient.checkId(id) + "/owners", Modification.cleanReferences(owners));
    }

    @Override
    public Result<?> addShares(String id, Reference... shares) {
        return worcadeClient.post(entityUrl + "/" + WorcadeClient.checkId(id) + "/sharedWith", Modification.cleanReferences(shares));
    }

    @Override
    public Result<?> setShares(String id, Reference... shares) {
        return worcadeClient.put(entityUrl + "/" + WorcadeClient.checkId(id) + "/sharedWith", Modification.cleanReferences(shares));
    }

    @Override
    public Result<?> removeShares(String id, Reference... shares) {
        return worcadeClient.delete(entityUrl + "/" + WorcadeClient.checkId(id) + "/sharedWith", Modification.cleanReferences(shares));
    }

    @Override
    public Result<? extends Collection<? extends Contact>> getContacts(Query<ContactField> query) {
        return worcadeClient.getList(entityUrl);
    }

    @Override
    public Result<? extends Collection<? extends Webhook>> getWebhookList(Query<WebhookField> query) {
        return getList(query);
    }

    @Override
    public Result<? extends Collection<? extends Webhook.Log>> getLogs(String id) {
        return worcadeClient.getList(entityUrl + "/" + WorcadeClient.checkId(id) + "/logs");
    }

    @Override
    public Result<? extends WebhookTestResult> requestTest(String id) {
        String url = entityUrl + "/test/" + WorcadeClient.checkId(id);
        return worcadeClient.post(url, null);
    }

    @Override
    public Result<? extends ReferenceWithName> searchByNumber(String conversationNumber) {
        return worcadeClient.get(entityUrl + "/number/" + Util.escapeUrlPathSegment(conversationNumber));
    }

    @Override
    public Result<? extends Collection<? extends ReferenceWithName>> searchByContent(String contentId) {
        return worcadeClient.getList(entityUrl + "/content/" + WorcadeClient.checkId(contentId));
    }

    @Override
    public Result<?> linkConversations(String firstId, String secondId) {
        return worcadeClient.post(entityUrl + "/linked/" + WorcadeClient.checkId(firstId) + "/" + WorcadeClient.checkId(secondId), null);
    }

    @Override
    public Result<?> addVersions(String id, String... versions) {
        return worcadeClient.post(entityUrl + "/" + WorcadeClient.checkId(id) + "/versions", ImmutableList.copyOf(versions));
    }

    @Override
    public Result<AttachmentData> getData(String id) {
        Response response = worcadeClient.getCustom(entityUrl + "/" + WorcadeClient.checkId(id) + "/data");
        if (response.getStatus() == 429) {
            return Result.failed(ImmutableList.of(new Result.Message(null, "Too many requests")));
        }
        return Result.ok(new AttachmentData(response.getHeaderString(HttpHeaders.CONTENT_DISPOSITION),
                (InputStream) response.getEntity(), response.getMediaType()));
    }

    @Override
    public Result<? extends Reference> create(String name, InputStream data, MediaType contentType) {
        checkArgument(!Strings.isNullOrEmpty(name), "Name is mandatory");
        String url = entityUrl + "?name=" + Util.escapeUrlQueryParameter(name);
        return worcadeClient.handle("POST", url, worcadeClient.postCustom(url, Entity.entity(data, contentType)))
                .map(WorcadeClient.DTO_FUNCTION);
    }

    @Override
    public Result<?> requestEmailChange(String id, String email, String password) {
        return worcadeClient.post(entityUrl + "/" + WorcadeClient.checkId(id) + "/email", ImmutableMap.of("email", email),
                new Header("Worcade-ConfirmPassword", Util.encodeBase64(password)));
    }

    @Override
    public Result<? extends Collection<? extends ReferenceWithName>> getMembers(String id, Query<MemberField> query) {
        return worcadeClient.getList(entityUrl + "/" + WorcadeClient.checkId(id) + ((WorcadeQuery<?>) query).toQueryString());
    }

    @Override
    public Result<?> addMembers(String id, Reference... members) {
        return worcadeClient.post(entityUrl + "/" + WorcadeClient.checkId(id) + "/members", Modification.cleanReferences(members));
    }

    @Override
    public Result<?> setMembers(String id, Reference... members) {
        return worcadeClient.put(entityUrl + "/" + WorcadeClient.checkId(id) + "/members", Modification.cleanReferences(members));
    }

    @Override
    public Result<?> removeMembers(String id, Reference... members) {
        return worcadeClient.delete(entityUrl + "/" + WorcadeClient.checkId(id) + "/members", Modification.cleanReferences(members));
    }

    @Override
    public Result<? extends ReferenceWithName> searchByDomain(String domain) {
        return worcadeClient.get(entityUrl + "/domain?domain=" + Util.escapeUrlQueryParameter(domain));
    }

    @Override
    public Result<?> addDomains(String id, String... domains) {
        return worcadeClient.post(entityUrl + "/" + WorcadeClient.checkId(id) + "/domains", ImmutableList.copyOf(domains));
    }

    @Override
    public Result<?> setDomains(String id, String... domains) {
        return worcadeClient.put(entityUrl + "/" + WorcadeClient.checkId(id) + "/domains", ImmutableList.copyOf(domains));
    }

    @Override
    public Result<?> removeDomains(String id, String... domains) {
        return worcadeClient.delete(entityUrl + "/" + WorcadeClient.checkId(id) + "/domains", ImmutableList.copyOf(domains));
    }

    @Override
    public Result<?> requestEmailReclaim(String email) {
        return worcadeClient.post(entityUrl + "/email?email=" + Util.escapeUrlQueryParameter(email), null);
    }

    @Override
    public Result<?> confirmEmailReclaim(String id, String secret) {
        return worcadeClient.delete(entityUrl + "/" + WorcadeClient.checkId(id) + "/email", new Header("Worcade-Secret", secret));
    }

    @Override
    public Result<?> changePassword(String id, String currentPassword, String newPassword) {
        return worcadeClient.post(entityUrl + "/" + WorcadeClient.checkId(id) + "/password",
                ImmutableMap.of("password", Util.encodeBase64(newPassword)),
                new Header("Worcade-ConfirmPassword", Util.encodeBase64(currentPassword)));
    }

    @Override
    public Result<?> requestPasswordReset(String id) {
        return worcadeClient.post(entityUrl + "/" + WorcadeClient.checkId(id) + "/password", null);
    }

    @Override
    public Result<?> requestPasswordResetByEmail(String email) {
        return worcadeClient.post(entityUrl + "/password/email?email=" + Util.escapeUrlQueryParameter(email), null);
    }

    @Override
    public Result<?> requestApplicationTrust(String userId, String applicationId) {
        return worcadeClient.post(entityUrl + "/" + WorcadeClient.checkId(userId) + "/trust/" + WorcadeClient.checkId(applicationId), null);
    }

    @Override
    public Result<?> requestJoinGroup(String userId) {
        return worcadeClient.post(entityUrl + "/" + WorcadeClient.checkId(userId) + "/requestJoinGroup", null);
    }

    @Override
    public Result<?> requestSubscription(String id) {
        return worcadeClient.post(entityUrl + "/" + WorcadeClient.checkId(id) + "/mailinglist", null);
    }

    @Override
    public Result<? extends Collection<? extends ReferenceWithName>> searchByText(SearchQuery<ConversationField> query) {
        return worcadeClient.getList(entityUrl + "/search" + ((WorcadeSearchQuery<?>) query).toQueryString());
    }

    @Override
    public Result<?> addRows(String id, WorkOrder.Row... rows) {
        return worcadeClient.post(entityUrl + "/" + WorcadeClient.checkId(id) + "/row", Modification.cleanRows(rows));
    }

    @Override
    public Result<?> updateRow(String workOrderId, WorkOrderRowModification subject) {
        Modification modification = ((Modification) subject);
        String id = (String) modification.getData().get("id");
        return worcadeClient.put(entityUrl + "/" + WorcadeClient.checkId(workOrderId) + "/row/" + WorcadeClient.checkId(id), ((Modification) subject).getData());
    }

    @Override
    public Result<?> removeRows(String id, String... rowIdsToRemove) {
        return removeRows(id, Arrays.asList(rowIdsToRemove));
    }

    @Override
    public Result<?> removeRows(String id, Collection<String> rowIdsToRemove) {
        return worcadeClient.delete(entityUrl + "/" + WorcadeClient.checkId(id) + "/row",
                rowIdsToRemove.stream().map(Modification::cleanReference).collect(ImmutableList.toImmutableList()));
    }

    private Result<IncomingDto> createInternal(EntityModification subject) {
        return worcadeClient.post(entityUrl, ((Modification) subject).getData());
    }

    private Result<?> updateInternal(EntityModification subject, String urlSuffix) {
        Modification modification = ((Modification) subject);
        String id = (String) modification.getData().get("id");
        return worcadeClient.put(entityUrl + "/" + WorcadeClient.checkId(id) + urlSuffix, modification.getData());
    }

    private Result<List<IncomingDto>> getList(Query<?> query) {
        return worcadeClient.getList(entityUrl + "/" + ((WorcadeQuery<?>) query).toQueryString());
    }
}
