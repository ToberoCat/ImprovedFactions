package io.github.toberocat.improvedFactions.core.world;

import org.jetbrains.annotations.NotNull;

public interface Chunk<R> {
    @NotNull World<?> getWorld();

    int getX();

    int getZ();

    @NotNull R getRaw();
}
