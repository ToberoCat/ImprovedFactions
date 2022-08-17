package io.github.toberocat.improvedFactions.core.claims.component;

import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public interface WorldClaim {
    default void disable() {

    }

    void add(@NotNull String registry, int x, int z);

    void remove(int x, int z);

    @NotNull Stream<Claim> getAllClaims();

    @NotNull String getRegistry(int x, int z);

}
