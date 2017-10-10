// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.query;

import net.worcade.client.internal.WorcadeSearchQuery;

public interface SearchQuery<T extends EntityField> extends Query<T> {
    static Builder<ConversationField> conversation() {
        return WorcadeSearchQuery.searchBuilder();
    }

    interface Builder<T extends EntityField> {
        /**
         * Search for the value in any of the available fields
         */
        Builder<T> searchAnyField(String value);

        /**
         * Search for the value in the given field.
         * For details on which fields can be searched, see the <a href="https://worcade.net/doc">Worcade API doc</a>
         */
        Builder<T> searchField(String field, String value);

        /**
         * Defines the amount of characters that may be different from the query, for the text to still match.
         */
        Builder<T> fuzzyFactor(int factor);

        SearchQuery<T> build();
    }
}
