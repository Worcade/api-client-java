// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.create;

import net.worcade.client.get.Reference;
import net.worcade.client.get.RemoteId;
import net.worcade.client.modify.RoomModification;

public interface RoomCreate extends RoomModification {
    @Override RoomCreate name(String name);
    @Override RoomCreate floor(String floor);
    @Override RoomCreate roomNumber(String roomNumber);
    @Override RoomCreate location(Reference location);
    RoomCreate labels(Reference... labels);
    RoomCreate remoteIds(RemoteId... remoteIds);
}
