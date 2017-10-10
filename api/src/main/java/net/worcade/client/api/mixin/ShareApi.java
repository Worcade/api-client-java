// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.api.mixin;

import net.worcade.client.Result;
import net.worcade.client.get.Reference;

public interface ShareApi {
    Result<?> addShares(String id, Reference... shares);
    Result<?> setShares(String id, Reference... shares);
    Result<?> removeShares(String id, Reference... shares);
}
