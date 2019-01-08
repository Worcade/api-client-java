// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.api.mixin;

import net.worcade.client.Result;
import net.worcade.client.get.OptionalField;

import java.util.Collection;

/**
 * Base class for optional field interactions
 */
public interface OptionalFieldsApi {
    Result<? extends Collection<? extends OptionalField>> getOptionalFields(String id);
    Result<?> addOptionalFields(String id, OptionalField... optionalFields);
    Result<?> removeOptionalFields(String id, OptionalField... optionalFields);
}
