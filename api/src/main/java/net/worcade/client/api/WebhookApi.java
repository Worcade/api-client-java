// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.api;

import net.worcade.client.Result;
import net.worcade.client.create.WebhookCreate;
import net.worcade.client.get.Reference;
import net.worcade.client.get.Webhook;
import net.worcade.client.get.WebhookTestResult;
import net.worcade.client.query.Query;
import net.worcade.client.query.WebhookField;

import java.util.Collection;

public interface WebhookApi {
    WebhookCreate createBuilder();

    /**
     * Create a new Webhook. Use the {@link #createBuilder()} method for a new, empty template.
     */
    Result<? extends Reference> create(String ownerId, WebhookCreate subject);
    Result<?> delete(String id);
    Result<? extends Collection<? extends Webhook>> getWebhookList(Query<WebhookField> query);
    Result<? extends Collection<? extends Webhook.Log>> getLogs(String id, boolean includeDeleted);

    /**
     * @param id The id of the webhook to test
     */
    Result<? extends WebhookTestResult> requestTest(String id);
}
