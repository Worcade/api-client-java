// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.get;

import java.util.Collection;
import java.util.Locale;

public interface Group extends Entity, ReferenceWithName {
    Locale getLocale();
    Collection<String> getDomains();
    Collection<? extends ReferenceWithName> getMembers();
    ReferenceWithName getPicture();
    ReferenceWithName getCompany();
}
