// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.modify;

import net.worcade.client.get.Reference;

import java.util.Locale;

public interface GroupModification extends EntityModification {
    GroupModification name(String name);
    GroupModification mailImportAddress(String mailImportAddress);
    GroupModification locale(Locale locale);
    GroupModification autoShareTarget(Reference target);
    GroupModification picture(Reference picture);
    GroupModification visible(boolean visible);
}
