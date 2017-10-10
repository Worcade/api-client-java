// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.create;

import net.worcade.client.get.Webhook;
import net.worcade.client.modify.EntityModification;

public interface WebhookCreate extends EntityModification {
    WebhookCreate url(String url);
    WebhookCreate event(Webhook.Event event);
    WebhookCreate suppressOwn(boolean suppress);
    WebhookCreate headers(Webhook.Header... headers);
}
