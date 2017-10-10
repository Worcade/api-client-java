// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.get;

import net.worcade.client.modify.GroupModification;

public interface GroupProfile extends Group {
    GroupModification modify();

    String getEmail();
    String getMailImportAddress();
    boolean isPro();
    ReferenceWithName getAutoShareTarget();
    boolean isVisible();
}
