package io.github.toberocat.core.utility.data.access;

import io.github.toberocat.core.utility.data.Table;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
            public @NotNull <R> AccessPipeline<C> write(@NotNull Table table, @NotNull R instance) {
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
            public @NotNull AccessPipelineResult<Stream<String>, C> listInTableStream(@NotNull Table table) {
                return new AccessPipelineResult<>(Stream.empty(), this);
            }

            @Override
            public @NotNull AccessPipelineResult<List<String>, C> listInTable(@NotNull Table table) {
                return new AccessPipelineResult<>(Collections.emptyList(), this);
            }

            @Override
            public @NotNull AccessPipelineResult<Boolean, C> has(@NotNull Table table, @NotNull String byKey) {
                return new AccessPipelineResult<>(false, this);
            }

            @Override
            public @NotNull <C1> AccessPipelineResult<C1, C> read(@NotNull Table table, @NotNull String byKey) {
                return new AccessPipelineResult<>(null, this);
            }
        };

    }

    @NotNull <R> AccessPipeline<T> write(@NotNull Table table, @NotNull R instance);

    @NotNull AccessPipeline<T> delete(@NotNull Table table, @NotNull String byKey);

    @NotNull AccessPipeline<T> restoreDefault();

    @NotNull AccessPipelineResult<Stream<String>, T> listInTableStream(@NotNull Table table);

    @NotNull AccessPipelineResult<List<String>, T> listInTable(@NotNull Table table);

    @NotNull AccessPipelineResult<Boolean, T> has(@NotNull Table table, @NotNull String byKey);

    <C> @NotNull AccessPipelineResult<C, T> read(@NotNull Table table, @NotNull String byKey);

    record AccessPipelineResult<C, T extends AbstractAccess<T>>(C item, @NotNull AccessPipeline<T> pipeline) {
    }
}
