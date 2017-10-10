// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.get;

import java.time.Instant;

public interface ConversationContent {
    String getType();
    Instant getTimestamp();

    /**
     * Get a reference to the entity who added the content.
     */
    ReferenceWithName getSource();

    /**
     * Get a reference to the content. Available for all types except `MESSAGE` and `EVALUATION`.
     */
    Reference getContent();

    /**
     * Get the text message for this content. Only available if {@link #getType()} returns `MESSAGE`.
     */
    String getMessage();

    /**
     * Get the rating for this evaluation. Only available if {@link #getType()} returns `EVALUATION`.
     * @return a rating between 1 and 5
     */
    int getRating();
}
