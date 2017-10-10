// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.get;

import java.time.Instant;
import java.util.Collection;

public interface Notification {
    String getType();
    Reference getTarget();
    Reference getSubject();
    Collection<? extends Tracking> getTrackings();

    interface Tracking {
        Instant getTimestamp();
        String getType();
    }
}
