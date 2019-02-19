// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.api.mixin;

import net.worcade.client.Result;
import net.worcade.client.get.Reference;

import java.util.Collection;

public interface EmailAddressApi {
    Result<?> addEmailAddress(String id, String... emails);
    Result<?> removeEmailAddress(String id, String... emails);
    Result<?> reRequestEmailConfirmation(String id, String email);
    Result<? extends Collection<? extends Reference>> searchByEmail(String email);
}
