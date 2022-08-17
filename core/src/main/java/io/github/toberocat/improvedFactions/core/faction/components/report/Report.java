package io.github.toberocat.improvedFactions.core.faction.components.report;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record Report(@NotNull UUID reporter, @NotNull String reason) {
}
