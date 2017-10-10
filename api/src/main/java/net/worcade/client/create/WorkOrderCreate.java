// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.create;

import net.worcade.client.get.WorkOrder;
import net.worcade.client.modify.EntityModification;

public interface WorkOrderCreate extends EntityModification {
    WorkOrderCreate name(String name);
    WorkOrderCreate rows(WorkOrder.Row... rows);
}
