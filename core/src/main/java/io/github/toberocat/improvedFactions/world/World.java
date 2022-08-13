package io.github.toberocat.improvedFactions.world;

import org.jetbrains.annotations.NotNull;

public interface World {
    @NotNull String getWorldName();

    @NotNull Chunk getChunkAt(int x, int z);
}
