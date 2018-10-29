// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.create;

import net.worcade.client.get.Reference;
import net.worcade.client.modify.ApplicationModification;

public interface ApplicationCreate extends ApplicationModification {
    @Override ApplicationCreate name(String name);
    @Override ApplicationCreate email(String email);
    ApplicationCreate versions(String... versions);
    ApplicationCreate fingerprint(String fingerprint);
    ApplicationCreate company(Reference company);
}
