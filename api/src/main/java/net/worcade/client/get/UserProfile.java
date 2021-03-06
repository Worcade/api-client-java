// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.get;

import net.worcade.client.modify.UserModification;

import java.util.Collection;

public interface UserProfile extends User {
    UserModification modify();

    boolean hasPassword();
    Collection<? extends ReferenceWithName> getProGroups();
    String getEmail();
    String getMailImportAddress();
    ReferenceWithName getAutoShareTarget();
    boolean isSuppressUpdateNotifications();
    boolean isSubscribedToGettingStarted();
    boolean isSubscribedToNewsletter();
    Collection<? extends ReferenceWithName> getTrustedApplications();
    Collection<? extends Email> getSecondaryEmails();
}
