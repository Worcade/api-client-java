// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.api;

import net.worcade.client.Result;

public interface ReclaimApi {
    Result<?> requestEmailReclaim(String email);
    Result<?> confirmEmailReclaim(String id, String secret);
}
