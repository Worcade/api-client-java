// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.api;

import net.worcade.client.Result;
import net.worcade.client.api.mixin.OwnerApi;
import net.worcade.client.api.mixin.RemoteIdsApi;
import net.worcade.client.create.GroupCreate;
import net.worcade.client.get.Group;
import net.worcade.client.get.GroupProfile;
import net.worcade.client.get.Reference;
import net.worcade.client.get.ReferenceWithName;
import net.worcade.client.modify.GroupModification;
import net.worcade.client.query.GroupField;
import net.worcade.client.query.MemberField;
import net.worcade.client.query.Query;

import java.util.Collection;

public interface GroupApi extends OwnerApi, RemoteIdsApi {
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

    Result<? extends Collection<? extends ReferenceWithName>> searchByEmail(String email);
    Result<?> requestEmailChange(String id, String email, String password);

    Result<? extends Collection<? extends ReferenceWithName>> getMembers(String id, Query<MemberField> query);
    Result<?> addMembers(String id, Reference... members);
    Result<?> setMembers(String id, Reference... members);
    Result<?> removeMembers(String id, Reference... members);

    Result<? extends ReferenceWithName> searchByDomain(String domain);
    Result<?> addDomains(String id, String... domains);
    Result<?> setDomains(String id, String... domains);
    Result<?> removeDomains(String id, String... domains);
}