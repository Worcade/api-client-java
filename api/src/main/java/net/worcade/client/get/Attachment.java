// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.get;

import javax.ws.rs.core.MediaType;

public interface Attachment extends Entity, ReferenceWithName {
    MediaType getMimeType();
    int getSize();
    Integer getHeight();
    Integer getWidth();
}
