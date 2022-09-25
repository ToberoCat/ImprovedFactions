package io.github.toberocat.improvedFactions.core.world;

import org.jetbrains.annotations.NotNull;

public interface World<R> {
    @NotNull String getWorldName();

    @NotNull Chunk<?> getChunkAt(int x, int z);

    @NotNull R getRaw();
}
