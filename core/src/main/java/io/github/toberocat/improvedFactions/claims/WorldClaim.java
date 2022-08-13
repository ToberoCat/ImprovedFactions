package io.github.toberocat.improvedFactions.claims;

import org.jetbrains.annotations.NotNull;

public interface WorldClaim {
    void add(@NotNull String registry, int x, int z);

    void remove(int x, int z);

    @NotNull String getRegistry(int x, int z);

}
