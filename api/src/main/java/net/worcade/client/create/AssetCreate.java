// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.create;

import net.worcade.client.get.Reference;
import net.worcade.client.get.RemoteId;
import net.worcade.client.modify.AssetModification;

public interface AssetCreate extends AssetModification {
    @Override AssetCreate name(String name);
    @Override AssetCreate type(String type);
    @Override AssetCreate make(String make);
    @Override AssetCreate model(String model);
    @Override AssetCreate specification(String specification);
    @Override AssetCreate serial(String serial);
    @Override AssetCreate notes(String notes);
    @Override AssetCreate location(Reference location);
    @Override AssetCreate picture(Reference picture);
    AssetCreate labels(Reference... labels);
    AssetCreate sharedWith(Reference... targets);
    AssetCreate remoteIds(RemoteId... remoteIds);
}
