// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.get;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.Collection;

public interface Webhook {
    String getUrl();
    String getEvent();
    boolean isSuppressOwn();
    Collection<? extends Header> getHeaders();

    interface Header {
        String getName();
        String getValue();
    }

    interface Log {
        int getStatus();
        Instant getTimestamp();
        String getResponseBody();
    }

    @AllArgsConstructor
    enum Event {
        CONVERSATION_CREATE("conversation.new"),
        CONVERSATION_UPDATE("conversation.update");

        @Getter private final String event;

        public static Event forName(String name) {
            for (Event event : values()) {
                if (event.event.equals(name)) {
                    return event;
                }
            }
            return null;
        }
    }
}
