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
            public @NotNull AccessPipeline<AbstractAccess> write(@NotNull Table table, @NotNull Object object) {
                return this;
            }

            @Override
            public @NotNull AccessPipelineResult<Stream<String>, AbstractAccess> listInTableStream(@NotNull Table table) {
                return new AccessPipelineResult<>(Stream.empty(), this);
            }

            @Override
            public @NotNull AccessPipelineResult<List<String>, AbstractAccess> listInTable(@NotNull Table table) {
                return new AccessPipelineResult<>(List.of(), this);
            }

            @Override
            public @NotNull <C> AccessPipelineResult<C, AbstractAccess> read(@NotNull Table table, @NotNull Class<C> clazz) {
                return new AccessPipelineResult<>(null, this);
            }
        };
    }

    @NotNull AccessPipeline<T> write(@NotNull Table table, @NotNull Object object);

    @NotNull AccessPipelineResult<Stream<String>, T> listInTableStream(@NotNull Table table);

    @NotNull AccessPipelineResult<List<String>, T> listInTable(@NotNull Table table);

    <C> @NotNull AccessPipelineResult<C, T> read(@NotNull Table table, @NotNull Class<C> clazz);

    record AccessPipelineResult<C, T extends AbstractAccess>(@Nullable C item,
                                                             @NotNull AccessPipeline<T> pipeline) {
    }
}
