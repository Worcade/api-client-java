// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client.internal;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.ToString;
import net.worcade.client.query.EntityField;
import net.worcade.client.query.Query;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@AllArgsConstructor(access = AccessLevel.PACKAGE) @ToString
public class WorcadeQuery<T extends EntityField> implements Query<T> {
    private final Set<EntityField> fields;
    private final Multimap<EntityField, String> filter;
    private final List<Order> order;
    private final int limit;
    private final String search;

    String toQueryString() {
        StringBuilder sb = new StringBuilder("?limit=" + limit);
        for (EntityField field : fields) {
            sb.append("&field=").append(field.name());
        }
        for (Map.Entry<? extends EntityField, String> filter : filter.entries()) {
            sb.append("&").append(filter.getKey().name()).append("=").append(Util.escapeUrlQueryParameter(filter.getValue()));
        }
        for (Order o : order) {
            sb.append("&order=").append(o.ascending ? "" : "-").append(o.field.name());
        }
        if (!Strings.isNullOrEmpty(search)) {
            sb.append("&search=").append(Util.escapeUrlQueryParameter(search));
        }
        return sb.toString();
    }

    public static <T extends Enum<T> & EntityField> Query.Builder<T> builder() {
        return new Builder<>();
    }

    private static class Builder<T extends Enum<T> & EntityField> implements Query.Builder<T> {
        private final Set<EntityField> fields = Sets.newHashSet();
        private final ImmutableMultimap.Builder<EntityField, String> filter = ImmutableMultimap.builder();
        private final ImmutableList.Builder<Order> order = ImmutableList.builder();
        private int limit = 10;
        private String search;

        @Override
        @SafeVarargs
        public final Builder<T> fields(T... fields) {
            Collections.addAll(this.fields, fields);
            return this;
        }

        @Override
        public Builder<T> fields(Collection<T> fields) {
            this.fields.addAll(fields);
            return this;
        }

        @Override
        public Builder<T> filter(T field, String value) {
            filter.put(field, value);
            return this;
        }

        @Override
        public Builder<T> order(T field, boolean ascending) {
            order.add(new Order(field, ascending));
            return this;
        }

        @Override
        public Builder<T> limit(int limit) {
            this.limit = Math.min(Math.max(1, limit), 100);
            return this;
        }

        @Override
        public Builder<T> search(String search) {
            this.search = search;
            return this;
        }


        @Override
        public WorcadeQuery<T> build() {
            return new WorcadeQuery<>(ImmutableSet.copyOf(fields), filter.build(), order.build(), limit, search);
        }
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE) @ToString
    static class Order {
        private final EntityField field;
        private final boolean ascending;
    }
}
