package io.github.toberocat.core.utility.data.access;

import io.github.toberocat.core.utility.data.Table;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Stream;

public interface AccessPipeline<T extends AbstractAccess> {
    static AccessPipeline<?> empty() {
        return new AccessPipeline<>() {
            @Override
            public @NotNull <R> AccessPipeline<AbstractAccess> write(@NotNull Table table, @NotNull R instance) {
                return this;
            }

            @Override
            public @NotNull AccessPipeline<AbstractAccess> delete(@NotNull Table table, @NotNull String byKey) {
                return this;
            }

            @Override
            public @NotNull AccessPipeline<AbstractAccess> restoreDefault() {
                return this;
            }

            @Override
            public @NotNull AccessPipelineResult<Stream<String>, AbstractAccess> listInTableStream(@NotNull Table table) {
                return new AccessPipelineResult<>(null, this);
            }

            @Override
            public @NotNull AccessPipelineResult<List<String>, AbstractAccess> listInTable(@NotNull Table table) {
                return new AccessPipelineResult<>(null, this);
            }

            @Override
            public @NotNull AccessPipelineResult<Boolean, AbstractAccess> has(@NotNull Table table, @NotNull String byKey) {
                return new AccessPipelineResult<>(false, this);
            }

            @Override
            public @NotNull <C> AccessPipelineResult<C, AbstractAccess> read(@NotNull Table table, @NotNull String byKey, @NotNull Class<C> clazz) {
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

    <C> @NotNull AccessPipelineResult<C, T> read(@NotNull Table table, @NotNull String byKey, @NotNull Class<C> clazz);

    record AccessPipelineResult<C, T extends AbstractAccess>(@Nullable C item,
                                                             @NotNull AccessPipeline<T> pipeline) {
    }
}
