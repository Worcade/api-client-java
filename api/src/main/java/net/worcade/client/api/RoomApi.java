// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.api;

import net.worcade.client.Result;
import net.worcade.client.api.mixin.LabelsApi;
import net.worcade.client.api.mixin.OwnerApi;
import net.worcade.client.api.mixin.RemoteIdsApi;
import net.worcade.client.api.mixin.ShareApi;
import net.worcade.client.create.RoomCreate;
import net.worcade.client.get.Reference;
import net.worcade.client.get.Room;
import net.worcade.client.modify.RoomModification;
import net.worcade.client.query.Query;
import net.worcade.client.query.RoomField;

import java.util.Collection;

public interface RoomApi extends LabelsApi, OwnerApi, RemoteIdsApi, ShareApi {
    RoomCreate createBuilder();

    Result<? extends Room> get(String id);
    /**
     * Create a new Room. Use the {@link #createBuilder()} method for a new, empty template.
     */
    Result<? extends Reference> create(RoomModification subject);
    Result<?> update(RoomModification subject);
    Result<?> delete(String id);
    Result<? extends Collection<? extends Room>> getRoomList(Query<RoomField> query);
}
