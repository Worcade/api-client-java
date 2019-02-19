// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.get;

import net.worcade.client.modify.ChecklistModification;
import net.worcade.client.modify.ChecklistRowModification;

import java.util.Collection;

public interface Checklist extends Entity, ReferenceWithName {
    ChecklistModification modify();

    @Override
    String getName();
    Collection<? extends Row> getRows();

    interface Row {
        ChecklistRowModification modify();

        String getId();
        String getName();
        boolean isChecked();
    }
}
