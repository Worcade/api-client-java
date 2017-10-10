// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.get;

import net.worcade.client.modify.SiteModification;

import java.util.Collection;

public interface Site extends Entity, ReferenceWithName {
    SiteModification modify();

    String getAddressLineOne();
    String getAddressLineTwo();
    String getPostalCode();
    String getCity();
    String getRegion();
    String getCountry();
    String getTelephone();
    Coordinates getCoordinates();
    Collection<? extends ReferenceWithName> getLabels();

    interface Coordinates {
        float getLatitude();
        float getLongitude();
    }
}
