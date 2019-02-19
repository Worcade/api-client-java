// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.api;

import net.worcade.client.Result;
import net.worcade.client.api.mixin.RemoteIdsApi;
import net.worcade.client.create.ChecklistCreate;
import net.worcade.client.get.Checklist;
import net.worcade.client.get.Reference;
import net.worcade.client.modify.ChecklistModification;
import net.worcade.client.modify.ChecklistRowModification;

import java.util.Collection;

public interface ChecklistApi extends RemoteIdsApi {
    ChecklistCreate createBuilder();

    Result<? extends Checklist> get(String id);
    Result<?> update(ChecklistModification subject);
    Result<? extends Reference> create(String conversationId, ChecklistCreate subject);

    Result<?> addRows(String id, Checklist.Row... rows);
    Result<?> updateRow(String checklistId, ChecklistRowModification subject);
    Result<?> removeRows(String id, String... rowIdsToRemove);
    Result<?> removeRows(String id, Collection<String> rowIdsToRemove);
}
