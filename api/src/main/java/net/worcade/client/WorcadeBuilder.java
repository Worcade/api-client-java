// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client;

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

    Result<? extends Worcade> build();
}
