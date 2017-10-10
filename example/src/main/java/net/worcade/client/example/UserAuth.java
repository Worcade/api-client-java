// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.example;

import lombok.extern.slf4j.Slf4j;
import net.worcade.client.Result;
import net.worcade.client.Worcade;
import net.worcade.client.api.UserApi;
import net.worcade.client.get.Reference;
import net.worcade.client.get.UserProfile;

@Slf4j
public class UserAuth {
    private static final String PASSWORD = "password";

    static Result<? extends UserProfile> createAndLoginUser(Worcade client) {
        UserApi userApi = client.getUserApi();

        // Create a user
        Reference reference = userApi.create(userApi.createBuilder()
                .name("My user")
                .password(PASSWORD)).getResult();

        // If creation succeeded, log the user in. If it failed, return the failed result.
        return client.loginUserById(reference.getId(), PASSWORD)
                .flatMap(r -> userApi.getProfile(r.getId()));
    }
}
