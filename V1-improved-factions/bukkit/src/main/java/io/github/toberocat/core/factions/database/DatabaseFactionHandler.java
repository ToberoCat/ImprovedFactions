package io.github.toberocat.core.factions.database;

import io.github.toberocat.core.factions.local.LocalFaction;
import io.github.toberocat.core.factions.FactionHandler;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DatabaseFactionHandler extends FactionHandler {
    @Override
    protected LocalFaction create(@NotNull String display, @NotNull Player owner) {
        return new DatabaseFaction(display, owner);
    }
}
