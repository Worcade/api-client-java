// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.example;

import lombok.extern.slf4j.Slf4j;
import net.worcade.client.Result;
import net.worcade.client.Worcade;
import net.worcade.client.api.ApplicationApi;
import net.worcade.client.get.ApplicationProfile;
import net.worcade.client.get.Reference;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

@Slf4j
public class ApplicationAuth {
    static Result<? extends ApplicationProfile> createAndLoginApplication(Worcade client) throws NoSuchAlgorithmException {
        ApplicationApi applicationApi = client.getApplicationApi();

        // Generate a key pair. For TOPdesk integrations, use the application id and keys stored in the `worcadeTopdeskKeys` table
        KeyPair keyPair = ApplicationApi.generateKeyPair();

        // Create an Application. Simple error handling: `getResult` will throw an exception if create failed.
        Reference appRef = applicationApi.create(applicationApi.createBuilder()
                .name("Worcade API client example app")
                .versions(Worcade.VERSION.toString()))
                .getResult();

        // Setup the login using your generated key pair. Save the Worcade public key somewhere.
        Result<PublicKey> setupResult = applicationApi.setupKeyExchange(appRef.getId(), keyPair.getPublic());

        // If setup was successful, login the application. Otherwise, return the failed result.
        return setupResult
                .flatMap(k -> client.loginApplication(appRef.getId(), keyPair.getPrivate(), k))
                .flatMap(r -> applicationApi.getProfile(r.getId()));
    }
}
