// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.api;

import net.worcade.client.Result;
import net.worcade.client.api.mixin.OwnerApi;
import net.worcade.client.api.mixin.RemoteIdsApi;
import net.worcade.client.create.CompanyCreate;
import net.worcade.client.get.Company;
import net.worcade.client.get.Reference;
import net.worcade.client.modify.CompanyModification;

public interface CompanyApi extends OwnerApi, RemoteIdsApi {
    CompanyCreate createBuilder();

    Result<? extends Company> get(String id);
    /**
     * Create a new Asset. Use the {@link #createBuilder()} method for a new, empty template.
     */
    Result<? extends Reference> create(CompanyModification subject);
    Result<?> update(CompanyModification subject);
    Result<?> delete(String id);
}
