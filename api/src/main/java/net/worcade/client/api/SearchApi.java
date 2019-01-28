// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.api;

import net.worcade.client.Result;
import net.worcade.client.get.Reference;
import net.worcade.client.get.ReferenceWithName;
import net.worcade.client.get.RemoteIdSearchResult;

import java.util.Collection;

public interface SearchApi {
    /**
     * Search for entities with remote ids for the current authenticated application.
     * @param remoteIdType Return all entities with the given type, regardless of value. If null, return all entities with a remote id.
     * @param remoteId Return all entities with the given id value. This parameter is ignored if `remoteIdType` is null.
     * @deprecated since 1.1 / API version 2.15.0. Use {@link #searchByOptionalField(String, String, String)} instead.
     */
    @Deprecated
    Result<? extends Collection<? extends RemoteIdSearchResult>> searchByRemoteId(String remoteIdType, String remoteId);
    Result<? extends Collection<? extends RemoteIdSearchResult>> searchByOptionalField(String ownerId, String name, String value);
    Result<? extends Collection<? extends ReferenceWithName>> searchByEmail(String email);
    Result<? extends Collection<? extends Reference>> searchText(String query);
    Result<? extends Collection<? extends Reference>> searchPrefix(String query);
}
