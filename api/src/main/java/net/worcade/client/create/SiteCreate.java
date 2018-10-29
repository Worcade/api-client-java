// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.create;

import net.worcade.client.get.Reference;
import net.worcade.client.get.RemoteId;
import net.worcade.client.modify.SiteModification;

public interface SiteCreate extends SiteModification {
    @Override
    SiteCreate name(String name);
    @Override
    SiteCreate addressLineOne(String addressLineOne);
    @Override
    SiteCreate addressLineTwo(String addressLineTwo);
    @Override
    SiteCreate postalCode(String postalCode);
    @Override
    SiteCreate city(String city);
    @Override
    SiteCreate region(String region);
    @Override
    SiteCreate country(String country);
    @Override
    SiteCreate telephone(String telephone);
    @Override
    SiteCreate coordinates(float latitude, float longitude);
    SiteCreate labels(Reference... labels);
    SiteCreate remoteIds(RemoteId... remoteIds);
}
