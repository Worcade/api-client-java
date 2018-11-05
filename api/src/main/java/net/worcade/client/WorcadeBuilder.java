// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client;

import net.worcade.client.exception.IncompatibleVersionException;

/**
 * See {@link Worcade#builder()} for how to obtain an instance
 */
public interface WorcadeBuilder {
    /**
     * The URL of the Worcade server. Defaults to https://worcade.net
     */
    WorcadeBuilder baseUrl(String url);

    /**
     * If your application is a simple script to read or update a few entities, and then exit,
     * it might be beneficial to disable this cache. If your app is more than a simple script,
     * please don't disable the cache.
     */
    WorcadeBuilder disableETagCache();

    /**
     * Creates a {@link Worcade} client instance, and checks its server url and version.
     * If the server cannot be reached, this method will return with a {@link Result}
     * with a {@link Result.Message} with {@link Code} {@link Code#NO_CONNECTION}.
     *
     * You can call this method multiple times, but a new client with its own cache will be created each time. To obtain an instance
     *  that reuses the client, but allows different authentication, use the {@link Worcade#copyWithSameAuth()} method.
     *
     * @throws IncompatibleVersionException If the server is of an earlier version than the client, or of a different major version.
     */
    Result<? extends Worcade> build();
}
