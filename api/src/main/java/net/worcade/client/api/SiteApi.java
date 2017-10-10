// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.api;

import net.worcade.client.Result;
import net.worcade.client.api.mixin.LabelsApi;
import net.worcade.client.api.mixin.OwnerApi;
import net.worcade.client.api.mixin.RemoteIdsApi;
import net.worcade.client.api.mixin.ShareApi;
import net.worcade.client.create.SiteCreate;
import net.worcade.client.get.Reference;
import net.worcade.client.get.Site;
import net.worcade.client.modify.SiteModification;
import net.worcade.client.query.Query;
import net.worcade.client.query.SiteField;

import java.util.Collection;

public interface SiteApi extends LabelsApi, OwnerApi, RemoteIdsApi, ShareApi {
    SiteCreate createBuilder();

    Result<? extends Site> get(String id);
    /**
     * Create a new Site. Use the {@link #createBuilder()} method for a new, empty template.
     */
    Result<? extends Reference> create(SiteModification subject);
    Result<?> update(SiteModification subject);
    Result<?> delete(String id);
    Result<? extends Collection<? extends Site>> getSiteList(Query<SiteField> query);
}
