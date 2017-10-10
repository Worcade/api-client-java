// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.exception;

public class InvalidValueTypeException extends IllegalStateException {
    public <T> InvalidValueTypeException(Class<T> expected, String key, Object actual) {
        super("Couldn't get value for " + key + ": Expected instance of " + expected.getSimpleName() + ", got " +
                (actual == null ? "null" : actual.getClass() + ": " + actual));
    }
}
