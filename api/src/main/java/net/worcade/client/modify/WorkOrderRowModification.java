// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.modify;

import java.time.Duration;
import java.util.Currency;

public interface WorkOrderRowModification extends EntityModification {
    WorkOrderRowModification description(String description);
    WorkOrderRowModification duration(Duration duration);
    WorkOrderRowModification cost(double amount, Currency currency);
}
