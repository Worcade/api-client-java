// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.modify;

import net.worcade.client.get.Reference;

import java.util.Locale;

public interface UserModification extends EntityModification {
    UserModification name(String name);
    UserModification locale(Locale locale);
    UserModification mailImportAddress(String mailImportAddress);
    UserModification password(String password);
    UserModification location(Reference location);
    UserModification autoShareTarget(Reference target);
    UserModification picture(Reference picture);
    UserModification suppressUpdateNotifications(boolean suppress);
    UserModification subscribeGettingStarted(boolean subscribe);
    UserModification subscribeNewsletter(boolean subscribe);
}
