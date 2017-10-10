// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.exception;

import net.worcade.client.Worcade;

public class IncompatibleVersionException extends IllegalStateException {
    public IncompatibleVersionException(String serverVersion) {
        super("Incompatible version: Library expects version " + Worcade.VERSION + ", server is version " + serverVersion);
    }
}
