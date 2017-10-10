// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.api;

import net.worcade.client.Result;
import net.worcade.client.api.mixin.LabelsApi;
import net.worcade.client.api.mixin.RemoteIdsApi;
import net.worcade.client.create.ConversationCreate;
import net.worcade.client.get.Conversation;
import net.worcade.client.get.ExternalNumber;
import net.worcade.client.get.Reference;
import net.worcade.client.get.ReferenceWithName;
import net.worcade.client.get.ReferenceWithNumber;
import net.worcade.client.modify.ConversationModification;
import net.worcade.client.query.ConversationField;
import net.worcade.client.query.Query;
import net.worcade.client.query.SearchQuery;

import java.util.Collection;

public interface ConversationApi extends LabelsApi, RemoteIdsApi {
    ConversationCreate createBuilder();

    Result<? extends Conversation> get(String id);
    /**
     * Create a new Conversation. Use the {@link #createBuilder()} method for a new, empty template.
     */
    Result<? extends ReferenceWithNumber> create(ConversationModification subject);
    Result<?> update(ConversationModification subject);
    Result<? extends Collection<? extends Conversation>> getConversationList(Query<ConversationField> query);

    Result<?> view(String id);

    Result<?> addMessage(String id, String message);
    Result<?> addContent(String id, Reference... content);
//    Result<?> addWorkOrder(String id, WorkOrderCreate workOrder);
    Result<?> addEvaluation(String id, int rating);

    Result<?> addExternalNumbers(String id, ExternalNumber... numbers);
    Result<?> linkConversations(String firstId, String secondId);

    Result<?> addWatchers(String id, Reference... watchers);
    Result<?> removeWatchers(String id, Reference... watchers);

    Result<? extends ReferenceWithName> searchByNumber(String conversationNumber);
    Result<? extends Collection<? extends ReferenceWithName>> searchByContent(String contentId);
    Result<? extends Collection<? extends ReferenceWithName>> searchByText(SearchQuery<ConversationField> query);
}
