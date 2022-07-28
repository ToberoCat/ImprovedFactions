package io.github.toberocat.core.factions.components.report;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record Report(@NotNull UUID reporter, @NotNull String reason) {
}
