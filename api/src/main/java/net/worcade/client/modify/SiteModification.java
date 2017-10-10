// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.modify;

public interface SiteModification extends EntityModification {
    SiteModification name(String name);
    SiteModification addressLineOne(String addressLineOne);
    SiteModification addressLineTwo(String addressLineTwo);
    SiteModification postalCode(String postalCode);
    SiteModification city(String city);
    SiteModification region(String region);
    SiteModification country(String country);
    SiteModification telephone(String telephone);
    SiteModification coordinates(float latitude, float longitude);
}
