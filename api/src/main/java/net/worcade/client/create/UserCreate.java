// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.create;

import net.worcade.client.get.Reference;
import net.worcade.client.get.RemoteId;
import net.worcade.client.modify.UserModification;

import java.util.Locale;

public interface UserCreate extends UserModification {
    @Override UserCreate name(String name);
    @Override
    UserCreate email(String email);
    @Override UserCreate locale(Locale locale);
    @Override UserCreate mailImportAddress(String mailImportAddress);
    @Override UserCreate password(String password);
    @Override UserCreate location(Reference location);
    @Override UserCreate autoShareTarget(Reference target);
    @Override UserCreate picture(Reference picture);
    @Override UserCreate suppressUpdateNotifications(boolean suppress);
    @Override UserCreate subscribeGettingStarted(boolean subscribe);
    @Override UserCreate subscribeNewsletter(boolean subscribe);
    UserCreate trustedApplications(Reference... applications);
    UserCreate remoteIds(RemoteId... remoteIds);
    UserCreate company(Reference company);
}
