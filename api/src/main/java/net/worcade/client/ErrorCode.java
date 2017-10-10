// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@AllArgsConstructor @Getter
public enum ErrorCode {
    EMAIL_RESERVED(1001),
    REMOTE_ID_USED(1002),
    MAILIMPORT_ADDRESS_RESERVED(1003),
    DOMAIN_RESERVED(1004),
    MAX_LOGIN_ATTEMPTS(2001),
    ;

    private final int code;

    public static ErrorCode forCode(Integer code) {
        for (ErrorCode errorCode : values()) {
            if (Objects.equals(code, errorCode.code)) return errorCode;
        }
        return null;
    }
}
