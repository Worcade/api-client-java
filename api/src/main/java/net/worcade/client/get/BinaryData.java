// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.get;

import lombok.Value;

import java.io.InputStream;

@Value
public class BinaryData {
    private final String fileName;
    private final InputStream data;
    private final String mimeType;
}
