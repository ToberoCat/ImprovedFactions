package io.github.toberocat.core.utility.data.access;

import io.github.toberocat.core.utility.data.Table;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public interface AccessPipeline<T extends AbstractAccess<T>> {
    static <C extends AbstractAccess<C>> AbstractAccess<C> empty() {
        return new AbstractAccess<>() {
            @Override
            public boolean register() {
                return false;
            }

            @Override
            public @NotNull <R> AccessPipeline<C> write(@NotNull Table table, @NotNull String byKey, @NotNull R instance) {
                return this;
            }

            @Override
            public @NotNull AccessPipeline<C> delete(@NotNull Table table, @NotNull String byKey) {
                return this;
            }

            @Override
            public @NotNull AccessPipeline<C> restoreDefault() {
                return this;
            }

            @Override
            public @NotNull Stream<String> listInTableStream(@NotNull Table table) {
                return Stream.empty();
            }

            @Override
            public @NotNull List<String> listInTable(@NotNull Table table) {
                return Collections.emptyList();
            }

            @Override
            public boolean has(@NotNull Table table, @NotNull String byKey) {
                return false;
            }

            @Override
            public <R> R read(@NotNull Table table, @NotNull String byKey) {
                return null;
            }
        };
    }

    @NotNull <R> AccessPipeline<T> write(@NotNull Table table, @NotNull String byKey, @NotNull R instance);

    @NotNull AccessPipeline<T> delete(@NotNull Table table, @NotNull String byKey);

    @NotNull AccessPipeline<T> restoreDefault();

    @NotNull Stream<String> listInTableStream(@NotNull Table table);

    @NotNull List<String> listInTable(@NotNull Table table);

    boolean has(@NotNull Table table, @NotNull String byKey);

    <R> R read(@NotNull Table table, @NotNull String byKey);

}
