// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.example;

import com.google.common.collect.Iterables;
import lombok.extern.slf4j.Slf4j;
import net.worcade.client.Worcade;
import net.worcade.client.api.AssetApi;
import net.worcade.client.api.ConversationApi;
import net.worcade.client.api.RoomApi;
import net.worcade.client.api.SiteApi;
import net.worcade.client.api.UserApi;
import net.worcade.client.api.WorkOrderApi;
import net.worcade.client.get.Reference;
import net.worcade.client.get.ReferenceWithNumber;
import net.worcade.client.get.UserProfile;
import net.worcade.client.get.WorkOrder;
import net.worcade.client.query.ConversationField;
import net.worcade.client.query.Query;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Currency;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@Slf4j
public class ConversationExample {
    public static final String DEFAULT_SERVER = "https://demo.worcade.net";

    public static void main(String[] args) {
        turnOffJul();

        Worcade client = Worcade.builder()
                .baseUrl(DEFAULT_SERVER)
                .disableETagCache()
                .build()
                .getResult();

        UserApi userApi = client.getUserApi();
        Reference you = userApi.create(userApi.createBuilder().name("You")).getResult();
        UserProfile me = UserAuth.createAndLoginUser(client).getResult();

        ConversationApi conversationApi = client.getConversationApi();
        WorkOrderApi workOrderApi = client.getWorkOrderApi();

        ReferenceWithNumber convo = conversationApi.create(conversationApi.createBuilder()
                .name("My convo")
                .externalNumbers(client.createExternalNumber("123", "desc"))
                .reporter(you)
                .assignee(me)
                .watchers(me, you))
                .getResult();

        conversationApi.getConversationList(Query.conversation()
                .limit(99)
                .fields(ConversationField.values())
                .filter(ConversationField.watchers, me.getId())
                .order(ConversationField.lastContent, false)
                .build()).getResult();

        conversationApi.addExternalNumbers(convo.getId(), client.createExternalNumber("456", "other tool")).getResult();
        conversationApi.addMessage(convo.getId(), "Hi there").getResult();
        WorkOrder order = workOrderApi.create(convo.getId(), workOrderApi.createBuilder().name("Order").rows(
                client.createWorkOrderRow("Row", Duration.of(5, ChronoUnit.MINUTES), 42.5, Currency.getInstance("EUR")),
                client.createWorkOrderRow("Other row", Duration.of(42, ChronoUnit.HOURS), -42.4, Currency.getInstance("USD"))))
                .flatMap(r -> workOrderApi.get(r.getId())).getResult();
        Reference asset = createAsset(client);
        conversationApi.addContent(convo.getId(), asset).getResult();
        conversationApi.get(convo.getId()).flatMap(c -> conversationApi.update(c.modify()
                .name("My closed convo")
                .reporter(me)
                .close())).getResult();
        workOrderApi.addRows(order.getId(), client.createWorkOrderRow("New row", null, null, null)).getResult();
        workOrderApi.removeRows(order.getId(), Iterables.getFirst(order.getRows(), null).getId()).getResult();
        workOrderApi.update(order.modify().approve()).getResult();
        conversationApi.addEvaluation(convo.getId(), 4).getResult();
        conversationApi.removeWatchers(convo.getId(), me).getResult();
        conversationApi.addWatchers(convo.getId(), me).getResult();
        conversationApi.view(convo.getId()).getResult();
        conversationApi.addMessage(convo.getId(), "I'm closing it now").getResult();
        conversationApi.get(convo.getId()).flatMap(c -> conversationApi.update(c.modify().close())).getResult();

        log.debug("Find it by number: {}", conversationApi.searchByNumber(convo.getNumber()).getResult());
        log.debug("Find it by content: {}", conversationApi.searchByContent(asset.getId()).getResult());
    }

    private static Reference createAsset(Worcade client) {
        AssetApi assetApi = client.getAssetApi();
        return assetApi.create(assetApi.createBuilder()
                .name("My asset")
                .type("type")
                .make("make")
                .model("model")
                .specification("spec")
                .serial("serial number")
                .location(createRoom(client)))
                .getResult();
    }

    private static Reference createRoom(Worcade client) {
        RoomApi roomApi = client.getRoomApi();
        return roomApi.create(roomApi.createBuilder()
                .name("My room")
                .floor("Second")
                .roomNumber("42a")
                .location(createSite(client)))
                .getResult();
    }

    private static Reference createSite(Worcade client) {
        SiteApi siteApi = client.getSiteApi();
        return siteApi.create(siteApi.createBuilder()
            .name("Worcade offices")
            .addressLineOne("Martinus Nijhofflaan 2")
            .addressLineTwo("12th floor south side")
            .postalCode("2624 ES")
            .city("Delft")
            .region("Zuid-Holland")
            .country("NL")
            .telephone("+31 (0)15 2700 900")
            .coordinates(51.9971101f, 4.3542369f)
        ).getResult();
    }

    private static void turnOffJul() {
        Logger root = LogManager.getLogManager().getLogger("");
        for (Handler handler : root.getHandlers()) {
            root.removeHandler(handler);
        }
    }
}
