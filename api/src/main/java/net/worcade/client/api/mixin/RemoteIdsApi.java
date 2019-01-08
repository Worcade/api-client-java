// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.api.mixin;

import net.worcade.client.Result;
import net.worcade.client.Worcade;
import net.worcade.client.get.Entity;
import net.worcade.client.get.RemoteId;
import net.worcade.client.get.RemoteIdSearchResult;

import java.util.Collection;

/**
 * Base class for remote id interactions
 * @deprecated since 1.1 / API version 2.15.0. Use the {@link OptionalFieldsApi} instead.
 */
@Deprecated
public interface RemoteIdsApi extends OptionalFieldsApi {
    /**
     * Search for entities with remote ids for the current authenticated application.
     * @param remoteIdType Return all entities with the given type, regardless of value. If null, return all entities with a remote id.
     * @param remoteId Return all entities with the given id value. This parameter is ignored if `remoteIdType` is null.
     * @deprecated since 1.1 / API version 2.15.0. Use the {@link OptionalFieldsApi} instead.
     */
    @Deprecated
    Result<? extends Collection<? extends RemoteIdSearchResult>> searchByRemoteId(String remoteIdType, String remoteId);

    /**
     * Add a remote id to the given entity. Remote id objects can be created using {@link Worcade#createRemoteId(String, String)}
     * @deprecated since 1.1 / API version 2.15.0. Use the {@link OptionalFieldsApi} instead.
     */
    @Deprecated
    Result<?> addRemoteIds(String id, RemoteId... remoteIds);
    /**
     * Remove a remote id from the given entity.
     * Remote id objects can be created using {@link Worcade#createRemoteId(String, String)}, or reused from {@link Entity#getRemoteIds()}
     * @deprecated since 1.1 / API version 2.15.0. Use the {@link OptionalFieldsApi} instead.
     */
    @Deprecated
    Result<?> removeRemoteIds(String id, RemoteId... remoteIds);
}
