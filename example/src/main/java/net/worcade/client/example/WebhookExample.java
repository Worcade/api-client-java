// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.example;

import lombok.extern.slf4j.Slf4j;
import net.worcade.client.Worcade;
import net.worcade.client.api.UserApi;
import net.worcade.client.api.WebhookApi;
import net.worcade.client.get.ApplicationProfile;
import net.worcade.client.get.Reference;
import net.worcade.client.get.Webhook;
import net.worcade.client.get.WebhookTestResult;

import java.security.NoSuchAlgorithmException;

@Slf4j
public class WebhookExample {
    public static void main(String[] args) throws NoSuchAlgorithmException, InterruptedException {
        Worcade client = Worcade.builder()
                .baseUrl(ConversationExample.DEFAULT_SERVER)
                .disableETagCache()
                .build()
                .getResult();

        ApplicationProfile app = ApplicationAuth.createAndLoginApplication(client).getResult();
        UserApi userApi = client.getUserApi();
        Reference me = userApi.create(userApi.createBuilder()
                .name("My trusted user")
                .trustedApplications(app)).getResult();
        client.setTrustedUser(me).getResult();
        userApi.getProfile(me.getId()).getResult();

        WebhookApi webhookApi = client.getWebhookApi();
        // Create a webhook. Update the URL if you want to see the content.
        Reference hook = webhookApi.create(me.getId(), webhookApi.createBuilder()
                .event(Webhook.Event.CONVERSATION_UPDATE)
                .suppressOwn(false)
                .url("https://worcade.free.beeceptor.com/" + app.getId()))
                .getResult();

        // Test the webhook
        WebhookTestResult testResult = webhookApi.requestTest(hook.getId()).getResult();
        log.debug("Test got {}: {}", testResult.getStatus(), testResult.getBody());

        // Trigger the webhook for real
        client.getConversationApi().create(client.getConversationApi().createBuilder().name("Testing the hook").watchers(me))
                .flatMap(r -> client.getConversationApi().addMessage(r.getId(), "Testmessage"))
                .getResult();

        // Wait for the server to process the webhook
        Thread.sleep(1000);

        // Get the result
        log.debug("Logs: {}", webhookApi.getLogs(hook.getId(), false).getResult());

        // Clean up after ourselves
        webhookApi.delete(hook.getId()).getResult();
    }
}
