// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.api;

import net.worcade.client.Result;
import net.worcade.client.get.Contact;
import net.worcade.client.query.ContactField;
import net.worcade.client.query.Query;

import java.util.Collection;

public interface ContactsApi {
    Result<? extends Collection<? extends Contact>> getContacts(Query<ContactField> query);
}
