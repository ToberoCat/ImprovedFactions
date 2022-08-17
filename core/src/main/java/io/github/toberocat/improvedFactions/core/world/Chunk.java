package io.github.toberocat.improvedFactions.core.world;

import io.github.toberocat.improvedFactions.core.persistent.component.PersistentWrapper;
import org.jetbrains.annotations.NotNull;

public interface Chunk<R> {
    @NotNull World<?> getWorld();

    @NotNull PersistentWrapper getDataContainer();

    int getX();

    int getZ();

    @NotNull R getRaw();
}
