// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.create;

import net.worcade.client.get.Reference;
import net.worcade.client.get.RemoteId;
import net.worcade.client.modify.GroupModification;

import java.util.Locale;

public interface GroupCreate extends GroupModification {
    @Override GroupCreate name(String name);
    GroupCreate email(String email);
    @Override GroupCreate mailImportAddress(String mailImportAddress);
    GroupCreate domains(String... domains);
    @Override GroupCreate locale(Locale locale);
    @Override GroupCreate autoShareTarget(Reference target);
    GroupCreate members(Reference... members);
    @Override GroupCreate picture(Reference picture);
    @Override GroupCreate company(Reference company);
    GroupCreate remoteIds(RemoteId... remoteIds);
    @Override GroupCreate visible(boolean visible);
}
