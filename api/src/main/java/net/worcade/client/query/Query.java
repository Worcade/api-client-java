// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.query;

import net.worcade.client.internal.WorcadeQuery;

import java.util.Collection;

public interface Query<T extends EntityField> {
    static Builder<AssetField> asset() {
        return WorcadeQuery.builder();
    }

    static Builder<ConversationField> conversation() {
        return WorcadeQuery.builder();
    }

    static Builder<GroupField> group() {
        return WorcadeQuery.builder();
    }

    static Builder<RoomField> room() {
        return WorcadeQuery.builder();
    }

    static Builder<SiteField> site() {
        return WorcadeQuery.builder();
    }

    interface Builder<T extends EntityField> {

        /**
         * Add the specified fields to the result
         */
        Builder<T> fields(T... fields);

        /**
         * Add the specified fields to the result
         */
        Builder<T> fields(Collection<T> fields);

        /**
         * Add the specified filter to the query.
         * Filters for different fields will be AND'ed
         * Filters on the same field with different value will be OR'ed
         *
         * Not all fields can be filtered. For details, see the <a href="https://worcade.net/doc">Worcade API doc</a>
         */
        Builder<T> filter(T field, String value);

        /**
         * Add an order to the query. Multiple orders can be specified.
         *
         * Not all fields can be used for ordering. For details, see the <a href="https://worcade.net/doc">Worcade API doc</a>
         */
        Builder<T> order(T field, boolean ascending);

        /**
         * Limit the amount of results returned.
         * Defaults to 10. Cannot exceed 100.
         */
        Builder<T> limit(int limit);

        Query<T> build();
    }
}
