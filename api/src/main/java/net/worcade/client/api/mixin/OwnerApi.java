// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.api.mixin;

import net.worcade.client.Result;
import net.worcade.client.get.Reference;

public interface OwnerApi {
    Result<?> addOwners(String id, Reference... owners);
    Result<?> setOwners(String id, Reference... owners);
    Result<?> removeOwners(String id, Reference... owners);
}
