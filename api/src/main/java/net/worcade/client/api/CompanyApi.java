// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.api;

import net.worcade.client.Result;
import net.worcade.client.api.mixin.OwnerApi;
import net.worcade.client.api.mixin.RemoteIdsApi;
import net.worcade.client.create.CompanyCreate;
import net.worcade.client.get.Company;
import net.worcade.client.get.CompanyProfile;
import net.worcade.client.get.Reference;
import net.worcade.client.get.SamlSettings;
import net.worcade.client.modify.CompanyModification;
import net.worcade.client.modify.UserModification;

import javax.annotation.Nullable;

public interface CompanyApi extends OwnerApi, RemoteIdsApi {
    CompanyCreate createBuilder();

    Result<? extends Company> get(String id);
    Result<? extends CompanyProfile> getProfile(String id);
    /**
     * Create a new Company. Use the {@link #createBuilder()} method for a new, empty template.
     */
    Result<? extends Reference> create(CompanyModification subject);
    Result<?> updateProfile(CompanyModification subject);

    Result<?> inviteUser(String companyId, String userId);
    Result<?> addUser(String companyId, String userId);
    Result<?> addApplication(String companyId, String applicationId);

    Result<?> updateMemberProfile(UserModification subject);
    Result<?> deleteMember(String userId);

    Result<?> addDomains(String companyId, String... domains);
    Result<?> removeDomains(String companyId, String... domains);

    Result<? extends SamlSettings> getSamlSettings(String companyId);
    Result<?> setSamlSettings(String companyId, String entityId, String ssoServiceUrl, String certificate, @Nullable String attributeName, boolean allowCreate);
    Result<?> setSamlSettings(String companyId, String entityId, String metadataUrl, @Nullable String attributeName, boolean allowCreate);
}
