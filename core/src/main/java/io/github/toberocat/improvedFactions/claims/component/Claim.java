package io.github.toberocat.improvedFactions.claims.component;

import org.jetbrains.annotations.NotNull;

public record Claim(@NotNull String registry, @NotNull String world, int x, int z) {
}
