package io.github.toberocat.improvedFactions.core.claims.zone;

import org.jetbrains.annotations.NotNull;

public record Zone(@NotNull String alias,
                   @NotNull String translationId,
                   @NotNull String registry,
                   int color, boolean managed,
                   boolean protection, boolean pvp) {

}
