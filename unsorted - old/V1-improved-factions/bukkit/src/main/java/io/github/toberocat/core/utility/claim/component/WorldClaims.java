package io.github.toberocat.core.utility.claim.component;

import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public abstract class WorldClaims {

    protected final String worldName;

    public WorldClaims(@NotNull String worldName) {
        this.worldName = worldName;
    }

    public static @NotNull String convertWorldName(@NotNull String worldName) {
        return worldName.toLowerCase()
                .replaceAll("-", "_")
                .replaceAll("[^a-z_]", "_");
    }

    public abstract void add(@NotNull Claim claim);
    public abstract void remove(@NotNull Claim claim);
    public abstract void remove(int x, int z);
    public abstract Stream<Claim> getClaims();
    public abstract void save();

    public @NotNull String getWorldName() {
        return worldName;
    }
}
