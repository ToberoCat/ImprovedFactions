package io.github.toberocat.improvedFactions.world;

import org.jetbrains.annotations.NotNull;

public interface Chunk {
    @NotNull World getWorld();

    @NotNull PersistentDataContainer getDataContainer();

    int getX();

    int getZ();
}
