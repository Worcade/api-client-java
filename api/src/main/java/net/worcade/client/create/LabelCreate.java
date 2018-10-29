// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.create;

import net.worcade.client.get.Reference;
import net.worcade.client.get.RemoteId;
import net.worcade.client.modify.LabelModification;

public interface LabelCreate extends LabelModification {
    @Override
    LabelCreate name(String name);
    @Override
    LabelCreate backgroundColor(String color);
    @Override
    LabelCreate foregroundColor(String color);
    LabelCreate forGroup(Reference group);
    LabelCreate remoteIds(RemoteId... remoteIds);
}
