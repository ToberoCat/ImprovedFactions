package io.github.toberocat.improvedFactions.claims;

import org.jetbrains.annotations.NotNull;

public interface ClaimHandler {
    void createWorldClaims();
    void disable();

    @NotNull WorldClaim getClaim(@NotNull World);
}
