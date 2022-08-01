package io.github.toberocat.improvedFactions.faction.components.report;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record Report(@NotNull UUID reporter, @NotNull String reason) {
}
