// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.api;

import net.worcade.client.Result;
import net.worcade.client.api.mixin.ApiKeysApi;
import net.worcade.client.api.mixin.RemoteIdsApi;
import net.worcade.client.create.ApplicationCreate;
import net.worcade.client.get.Application;
import net.worcade.client.get.ApplicationProfile;
import net.worcade.client.get.CreateWithApiKey;
import net.worcade.client.get.Reference;
import net.worcade.client.modify.ApplicationModification;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Collection;

public interface ApplicationApi extends ApiKeysApi, RemoteIdsApi {
    ApplicationCreate createBuilder();

    Result<? extends Application> get(String id);
    Result<? extends ApplicationProfile> getProfile(String id);
    /**
     * Create a new Application. Use the {@link #createBuilder()} method for a new, empty template.
     */
    Result<? extends Reference> create(ApplicationModification subject);
    Result<CreateWithApiKey> createWithApiKey(ApplicationModification subject, String apiKeyDescription);
    Result<?> updateProfile(ApplicationModification subject);
    Result<PublicKey> setupKeyExchange(String id, PublicKey applicationKey);
    Result<?> addVersions(String id, String... versions);
    Result<? extends Collection<? extends Reference>> getByFingerprint(String fingerprint);

    /**
     * Helper method to generate a proper keypair used for application authentication.
     * Keep the private key somewhere safe, and send the public key to Worcade using {@link #setupKeyExchange(String, PublicKey)}
     *
     * @return a securely generated key pair
     * @throws NoSuchAlgorithmException if there's no provider for the RSA algorithm
     */
    static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048, new SecureRandom());
        return generator.generateKeyPair();
    }
}
