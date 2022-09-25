package io.github.toberocat.improvedFactions.core.location;

import org.jetbrains.annotations.NotNull;

public record Location(double x, double y, double z, @NotNull String world) {
    public int chunkX() {
        return (int) Math.floor(x / 16);
    }

    public int chunkZ() {
        return (int) Math.floor(z / 16);
    }
}
