// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.create;

import net.worcade.client.get.ExternalNumber;
import net.worcade.client.get.Reference;
import net.worcade.client.get.RemoteId;
import net.worcade.client.modify.ConversationModification;

import java.util.Collection;

public interface ConversationCreate extends ConversationModification {
    @Override ConversationCreate name(String name);
    @Override ConversationCreate assignee(Reference assignee);
    @Override ConversationCreate reporter(Reference reporter);
    ConversationCreate testMode(boolean testMode);
    ConversationCreate externalNumbers(ExternalNumber... numbers);
    ConversationCreate watchers(Reference... watchers);
    ConversationCreate watchers(Collection<? extends Reference> watchers);
    ConversationCreate remoteIds(RemoteId... remoteIds);
}
