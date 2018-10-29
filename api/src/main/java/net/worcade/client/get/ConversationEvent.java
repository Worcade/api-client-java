// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.get;

import java.time.Instant;

public interface ConversationEvent {
    String getType();
    Instant getTimestamp();

    String getName();

    /**
     * Get a reference to the entity who added the content.
     */
    ReferenceWithName getSource();

    /**
     * Get a reference to the content. Available for all types except `MESSAGE` and `EVALUATION`.
     */
    ReferenceWithName getSubject();

    String getContext();
}
