package io.github.toberocat.improvedFactions.world;

import io.github.toberocat.improvedFactions.persistent.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;

public interface Chunk {
    @NotNull World getWorld();

    @NotNull PersistentDataContainer getDataContainer();

    int getX();

    int getZ();
}
