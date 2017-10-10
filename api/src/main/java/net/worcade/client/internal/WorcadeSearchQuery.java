// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.internal;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import lombok.ToString;
import net.worcade.client.query.EntityField;
import net.worcade.client.query.SearchQuery;

import java.util.Set;

@ToString
public class WorcadeSearchQuery<T extends EntityField> extends WorcadeQuery<T> implements SearchQuery<T> {
    private final int fuzzyFactor;

    private WorcadeSearchQuery(Set<EntityField> fields, Multimap<EntityField, String> filter, int limit, int fuzzyFactor) {
        super(fields, filter, ImmutableList.of(), limit);
        this.fuzzyFactor = fuzzyFactor;
    }

    String toQueryString() {
        StringBuilder sb = new StringBuilder(super.toQueryString());
        sb.append("&fuzzy=").append(fuzzyFactor);
        return sb.toString();
    }

    public static <T extends Enum<T> & EntityField> SearchQuery.Builder<T> searchBuilder() {
        return new Builder<>();
    }

    static class Builder<T extends Enum<T> & EntityField> extends WorcadeQuery.Builder<T> implements SearchQuery.Builder<T> {
        private int fuzzyFactor = 0;

        @Override
        public Builder<T> order(T field, boolean ascending) {
            throw new IllegalStateException("The Order field is not available for search queries");
        }

        @Override
        public Builder<T> searchAnyField(String value) {
            filter.put(() -> "any", value);
            return this;
        }

        @Override
        public Builder<T> searchField(String field, String value) {
            filter.put(() -> field, value);
            return this;
        }

        @Override
        public Builder<T> fuzzyFactor(int factor) {
            this.fuzzyFactor = factor;
            return this;
        }

        @Override
        public WorcadeSearchQuery<T> build() {
            return new WorcadeSearchQuery<>(ImmutableSet.copyOf(fields), filter.build(), limit, fuzzyFactor);
        }
    }
}
