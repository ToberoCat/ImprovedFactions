package io.github.toberocat.improvedFactions.core.claims.worldclaim;

import io.github.toberocat.improvedFactions.core.claims.component.Claim;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public interface WorldClaim {
    default void dispose() {

    }

    void add(@NotNull String registry, int x, int z);

    void remove(int x, int z);

    @NotNull Stream<Claim> getAllClaims();

    @Nullable String getRegistry(int x, int z);

}
