// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.api;

import net.worcade.client.Result;
import net.worcade.client.api.mixin.LabelsApi;
import net.worcade.client.api.mixin.OwnerApi;
import net.worcade.client.api.mixin.RemoteIdsApi;
import net.worcade.client.api.mixin.ShareApi;
import net.worcade.client.create.AssetCreate;
import net.worcade.client.get.Asset;
import net.worcade.client.get.Reference;
import net.worcade.client.modify.AssetModification;
import net.worcade.client.query.AssetField;
import net.worcade.client.query.Query;

import java.util.Collection;

public interface AssetApi extends LabelsApi, OwnerApi, RemoteIdsApi, ShareApi {
    AssetCreate createBuilder();

    Result<? extends Asset> get(String id);
    /**
     * Create a new Asset. Use the {@link #createBuilder()} method for a new, empty template.
     */
    Result<? extends Reference> create(AssetModification subject);
    Result<?> update(AssetModification subject);
    Result<?> delete(String id);

    Result<? extends Collection<? extends Asset>> getAssetList(Query<AssetField> query);
}
