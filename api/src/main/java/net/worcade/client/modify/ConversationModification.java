// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.modify;

import net.worcade.client.get.Reference;

public interface ConversationModification extends EntityModification {
    ConversationModification name(String name);
    ConversationModification reporter(Reference reporter);
    ConversationModification assignee(Reference assignee);
    ConversationModification testMode(boolean testMode);
    ConversationModification close();
    ConversationModification reopen();
    ConversationModification archive();
    ConversationModification unarchive();
}
