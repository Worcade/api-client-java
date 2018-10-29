// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.api;

import net.worcade.client.Result;
import net.worcade.client.api.mixin.RemoteIdsApi;
import net.worcade.client.create.WorkOrderCreate;
import net.worcade.client.get.Reference;
import net.worcade.client.get.WorkOrder;
import net.worcade.client.modify.WorkOrderModification;
import net.worcade.client.modify.WorkOrderRowModification;

import java.util.Collection;

public interface ChecklistApi extends RemoteIdsApi {
    WorkOrderCreate createBuilder();

    Result<? extends WorkOrder> get(String id);
    Result<?> update(WorkOrderModification subject);
    /**
     * Create a new Work order in the given conversation. Use the {@link #createBuilder()} method for a new, empty template.
     */
    Result<? extends Reference> create(String conversationId, WorkOrderCreate subject);

    Result<?> addRows(String id, WorkOrder.Row... rows);
    Result<?> updateRow(String workOrderId, WorkOrderRowModification subject);
    Result<?> removeRows(String id, String... rowIdsToRemove);
    Result<?> removeRows(String id, Collection<String> rowIdsToRemove);
}
