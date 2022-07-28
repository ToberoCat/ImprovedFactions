package io.github.toberocat.core.factions.components.report;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public record Report(@NotNull Player reporter, @NotNull String reason) {
}
