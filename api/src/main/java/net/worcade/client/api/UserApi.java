// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.api;

import net.worcade.client.Result;
import net.worcade.client.api.mixin.ApiKeysApi;
import net.worcade.client.api.mixin.EmailAddressApi;
import net.worcade.client.api.mixin.RemoteIdsApi;
import net.worcade.client.create.UserCreate;
import net.worcade.client.get.CreateWithApiKey;
import net.worcade.client.get.Reference;
import net.worcade.client.get.User;
import net.worcade.client.get.UserProfile;
import net.worcade.client.modify.UserModification;

public interface UserApi extends ApiKeysApi, EmailAddressApi, RemoteIdsApi {
    UserCreate createBuilder();

    Result<? extends User> get(String id);
    Result<? extends UserProfile> getProfile(String id);
    /**
     * Create a new User. Use the {@link #createBuilder()} method for a new, empty template.
     */
    Result<? extends Reference> create(UserModification subject);
    Result<CreateWithApiKey> createWithApiKey(UserModification subject, String apiKeyDescription);
    Result<?> updateProfile(UserModification subject);
    Result<?> cancelAccount(String id, String password);

    Result<?> changePassword(String id, String currentPassword, String newPassword);
    Result<?> requestPasswordReset(String id);
    Result<?> requestPasswordResetByEmail(String email);

    Result<?> requestApplicationTrust(String userId, String applicationId);
    Result<?> unlinkApplication(String userId, String applicationId);
}
