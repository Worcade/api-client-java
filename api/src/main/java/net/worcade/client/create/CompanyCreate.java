// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.create;

import net.worcade.client.get.Reference;
import net.worcade.client.get.RemoteId;
import net.worcade.client.modify.CompanyModification;

public interface CompanyCreate extends CompanyModification {
    @Override CompanyCreate name(String name);
    @Override CompanyCreate description(String description);
    @Override CompanyCreate url(String url);
    @Override CompanyCreate picture(Reference picture);
    @Override CompanyCreate location(Reference location);
    @Override CompanyCreate mainGroup(Reference group);
    CompanyCreate domains(String... domains);
    CompanyCreate remoteIds(RemoteId... remoteIds);
}
