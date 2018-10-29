// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.get;

import java.time.Instant;
import java.util.Collection;

public interface Entity extends Reference {
    Collection<? extends RemoteId> getRemoteIds();
    Collection<? extends ReferenceWithName> getOwners();
    Instant created();
    ReferenceWithName createdBy();
    ReferenceWithName modifier();
}
