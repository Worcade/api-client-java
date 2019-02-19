// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.api;

import net.worcade.client.Result;
import net.worcade.client.api.mixin.EmailAddressApi;
import net.worcade.client.api.mixin.OwnerApi;
import net.worcade.client.api.mixin.RemoteIdsApi;
import net.worcade.client.create.GroupCreate;
import net.worcade.client.get.Group;
import net.worcade.client.get.GroupProfile;
import net.worcade.client.get.Reference;
import net.worcade.client.modify.GroupModification;
import net.worcade.client.query.GroupField;
import net.worcade.client.query.Query;

import java.util.Collection;

public interface GroupApi extends EmailAddressApi, OwnerApi, RemoteIdsApi {
    GroupCreate createBuilder();

    Result<? extends Group> get(String id);
    Result<? extends GroupProfile> getProfile(String id);
    /**
     * Create a new Group. Use the {@link #createBuilder()} method for a new, empty template.
     */
    Result<? extends Reference> create(GroupModification subject);
    Result<?> updateProfile(GroupModification subject);
    Result<?> delete(String id);

    /**
     * Get a filtered list of groups.
     * You can create a Query object using {@link Query#group()}
     */
    Result<? extends Collection<? extends Group>> getGroupList(Query<GroupField> query);

    Result<?> addMembers(String id, Reference... members);
    Result<?> addMembers(String id, Collection<? extends Reference> members);
    Result<?> removeMembers(String id, Reference... members);
}
