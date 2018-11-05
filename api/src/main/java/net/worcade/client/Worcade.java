// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client;

import com.google.common.collect.Iterables;
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
import net.worcade.client.api.mixin.RemoteIdsApi;
import net.worcade.client.get.Authentication;
import net.worcade.client.get.ExternalNumber;
import net.worcade.client.get.Notification;
import net.worcade.client.get.Reference;
import net.worcade.client.get.ReferenceWithName;
import net.worcade.client.get.RemoteId;
import net.worcade.client.get.Webhook;
import net.worcade.client.get.WorkOrder;

import javax.annotation.CheckReturnValue;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Duration;
import java.util.Collection;
import java.util.Currency;
import java.util.ServiceLoader;
import java.util.function.Supplier;

/**
 * Entry point to the Worcade API client library.
 * Create a Worcade client using either of the #create methods
 */
@CheckReturnValue
public interface Worcade {
    /**
     * The minimal Worcade API version for this version of the client library.
     * The library is compatible with all later minor and patch versions, but not with later major versions.
     */
    Version VERSION = new Version(2,13,0);

    static WorcadeBuilder builder() {
        return ServiceLoader.load(WorcadeBuilder.class).iterator().next();
    }

    /**
     * Login a user by email address. This call replaces all existing authentications.
     * @return A reference to the logged in user
     */
    Result<ReferenceWithName> loginUserByEmail(String email, String password);

    /**
     * Login a user by id. This call replaces all existing authentications.
     * @return A reference to the logged in user
     */
    Result<ReferenceWithName> loginUserById(String userId, String password);

    Result<ReferenceWithName> setUserApiKey(String apiKey);

    /**
     * Logout the current logged in user, invalidating the session token.
     */
    Result<?> logoutUser();

    /**
     * Login an application. This call replaces all existing authentications.
     * @return A reference to the logged in application
     */
    Result<ReferenceWithName> loginApplication(String applicationId, PrivateKey applicationPrivateKey, PublicKey worcadePublicKey);

    Result<ReferenceWithName> setApplicationApiKey(String apiKey);

    /**
     * Logout the current logged in user, invalidating the session token.
     */
    Result<?> logoutApplication();

    Result<ReferenceWithName> setApplicationSourceAuth(String id);

    /**
     * Set a trusted user for the logged in application
     * @return a failed result if the application does not trust the user, and a successful one otherwise
     * @throws IllegalStateException if there is no logged in application
     */
    Result<? extends Authentication> setTrustedUser(Reference user);

    /**
     * Checks if the given user trusts the given application. The application must be authenticated to use this endpoint.
     * @return a result of `true` if the user trusts the application
     */
    Result<Boolean> probeUserTrust(String userId, String applicationId);

    Result<? extends Authentication> getAuthentication();

    Result<? extends Collection<? extends Notification>> getNotifications();

    RemoteId createRemoteId(String remoteIdType, String remoteId);
    ExternalNumber createExternalNumber(String number, String description);
    Webhook.Header createWebhookHeader(String name, String value);
    WorkOrder.Row createWorkOrderRow(String name, Duration duration, Double cost, Currency currency);

    ApplicationApi getApplicationApi();
    AssetApi getAssetApi();
    AttachmentApi getAttachmentApi();
    ChecklistApi getChecklistApi();
    CompanyApi getCompanyApi();
    ContactsApi getContactsApi();
    ConversationApi getConversationApi();
    GroupApi getGroupApi();
    LabelApi getLabelApi();
    ReclaimApi getReclaimApi();
    RoomApi getRoomApi();
    SearchApi getSearchApi();
    SiteApi getSiteApi();
    UserApi getUserApi();
    WebhookApi getWebhookApi();
    WorkOrderApi getWorkOrderApi();


    Worcade copyWithSameAuth();

    void close();

    static <T> Result<T> withRetry(Supplier<Result<T>> method) {
        Result<T> result = method.get();
        if (!result.isOk() && Iterables.all(result.getMessages(), m -> m.isCode(Code.CONFLICT_ALLOW_RETRY))) {
            return withRetry(method);
        }
        return result;
    }
}
