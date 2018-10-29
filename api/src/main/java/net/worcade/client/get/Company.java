// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.get;

import net.worcade.client.modify.CompanyModification;

import java.util.Collection;

public interface Company extends Entity, ReferenceWithName {
    CompanyModification modify();

    String getDescription();
    String getUrl();
    ReferenceWithName getPicture();
    ReferenceWithName getLocation();
    Collection<? extends ReferenceWithName> getLabels();
    boolean hasSaml();
}
