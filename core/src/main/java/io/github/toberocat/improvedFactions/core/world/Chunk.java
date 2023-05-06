package io.github.toberocat.improvedFactions.core.world;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Chunk {
    @NotNull World getWorld();
    @NotNull String getClaimRegistry();

    int getX();

    int getZ();

    @NotNull Object getRaw();
}
