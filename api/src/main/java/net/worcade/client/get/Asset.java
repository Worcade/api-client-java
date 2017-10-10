// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.get;

import net.worcade.client.modify.AssetModification;

import java.util.Collection;

public interface Asset extends Entity, ReferenceWithName {
    AssetModification modify();

    String getAssetType();
    String getAssetMake();
    String getAssetModel();
    String getSerial();
    String getSpecification();
    Collection<? extends ReferenceWithName> getLabels();
}
