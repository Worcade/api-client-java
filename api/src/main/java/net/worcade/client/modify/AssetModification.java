// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.modify;

import net.worcade.client.get.Reference;

public interface AssetModification extends EntityModification {
    AssetModification name(String name);
    AssetModification type(String type);
    AssetModification make(String make);
    AssetModification model(String model);
    AssetModification serial(String serial);
    AssetModification specification(String specification);
    AssetModification notes(String notes);
    AssetModification location(Reference location);
    AssetModification picture(Reference picture);
}
