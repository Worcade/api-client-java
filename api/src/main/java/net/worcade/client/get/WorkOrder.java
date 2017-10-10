// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.get;

import net.worcade.client.modify.WorkOrderModification;

import java.time.Duration;
import java.util.Collection;
import java.util.Currency;

public interface WorkOrder extends Entity, ReferenceWithName {
    WorkOrderModification modify();

    Collection<? extends Row> getRows();
    boolean isApproved();
    boolean isRejected();

    interface Row {
        String getId();
        String getDescription();
        Duration getDuration();
        Double getCostAmount();
        Currency getCostCurrency();
    }
}
