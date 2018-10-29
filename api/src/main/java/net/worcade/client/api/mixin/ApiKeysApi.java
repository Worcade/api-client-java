// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.api.mixin;

import net.worcade.client.Result;
import net.worcade.client.get.ApiKey;

import java.util.Collection;

public interface ApiKeysApi {
    Result<String> createApiKey(String id, String description);
    Result<? extends Collection<? extends ApiKey>> getApiKeys(String id);
    Result<?> removeApiKey(String id, String keyId);
}
