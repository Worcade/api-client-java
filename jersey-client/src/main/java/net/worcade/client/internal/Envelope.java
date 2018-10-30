// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.internal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
class Envelope {
    private Object data;
    private List<Message> messages;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Message {
        private Integer code;
        private String message;
    }
}
