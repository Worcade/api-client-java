// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.modify;

import net.worcade.client.get.Reference;

public interface RoomModification extends EntityModification {
    RoomModification name(String name);
    RoomModification floor(String floor);
    RoomModification roomNumber(String roomNumber);
    RoomModification location(Reference location);
}
