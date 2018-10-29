// Copyright (c) 2017, Worcade. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a MIT-style
// license that can be found in the LICENSE file.

package net.worcade.client;

import com.google.common.collect.ImmutableList;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.function.Function;

import static com.google.common.base.Preconditions.*;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(doNotUseGetters = true)
public class Result<T> {
    public static <T> Result<T> ok(T result, List<Message> messages) {
        return new Result<>(true, ImmutableList.copyOf(messages), result);
    }

    public static <T> Result<T> ok(T result) {
        return new Result<>(true, ImmutableList.of(), result);
    }

    public static <T> Result<T> failed(List<Message> messages) {
        return new Result<>(false, ImmutableList.copyOf(messages), null);
    }

    @AllArgsConstructor
    public static class Message {
        private final Code code;
        @Getter private final String message;

        public boolean hasCode() {
            return code != null;
        }

        public boolean isCode(Code code) {
            return this.code == checkNotNull(code);
        }

        public Code getCode() {
            checkState(hasCode(), "This message does not have a code");
            return code;
        }

        @Override
        public String toString() {
            return hasCode() ? code + ": " + message : message;
        }
    }

    @Getter private final boolean ok;
    @Getter private final List<Message> messages;
    private final T result;

    @CanIgnoreReturnValue
    public T getResult() {
        checkState(ok, "Action failed with %s, cannot get result", messages);
        return result;
    }

    public <M> Result<M> map(Function<T, M> mapper) {
        if (!isOk()) {
            return cast();
        }
        return ok(mapper.apply(result), messages);
    }

    public <M> Result<M> flatMap(Function<T, Result<M>> mapper) {
        if (!isOk()) {
            return cast();
        }
        Result<M> secondResult = mapper.apply(result);
        if (messages.isEmpty()) {
            return secondResult;
        }
        ImmutableList<Message> combined = ImmutableList.<Message>builder().addAll(messages).addAll(secondResult.getMessages()).build();
        return new Result<>(secondResult.ok, combined, secondResult.result);
    }

    public <V> Result<V> cast() {
        if (result == null) {
            @SuppressWarnings("unchecked") Result<V> castResult = (Result<V>) this;
            return castResult;
        }
        return new Result<>(ok, messages, null);
    }
}
