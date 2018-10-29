// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.modify;

import net.worcade.client.get.Reference;

public interface CompanyModification extends EntityModification {
    CompanyModification name(String name);
    CompanyModification description(String description);
    CompanyModification url(String url);
    CompanyModification picture(Reference picture);
    CompanyModification location(Reference location);
    CompanyModification mainGroup(Reference group);
}
