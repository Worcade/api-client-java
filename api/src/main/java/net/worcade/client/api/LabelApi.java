// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.api;

import net.worcade.client.Result;
import net.worcade.client.api.mixin.RemoteIdsApi;
import net.worcade.client.create.LabelCreate;
import net.worcade.client.get.Label;
import net.worcade.client.get.Reference;
import net.worcade.client.get.Room;
import net.worcade.client.modify.LabelModification;
import net.worcade.client.query.LabelField;
import net.worcade.client.query.Query;

import java.util.Collection;

public interface LabelApi extends RemoteIdsApi {
    LabelCreate createBuilder();

    Result<? extends Label> get(String id);
    /**
     * Create a new Room. Use the {@link #createBuilder()} method for a new, empty template.
     */
    Result<? extends Reference> create(LabelModification subject);
    Result<?> update(LabelModification subject);
    Result<?> delete(String id);
    Result<? extends Collection<? extends Room>> getLabelList(Query<LabelField> query);
}
