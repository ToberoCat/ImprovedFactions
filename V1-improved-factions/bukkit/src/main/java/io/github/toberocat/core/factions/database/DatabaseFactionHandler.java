package io.github.toberocat.core.factions.database;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.handler.FactionHandlerInterface;
import io.github.toberocat.core.utility.exceptions.faction.FactionNotInStorage;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class DatabaseFactionHandler implements FactionHandlerInterface<DatabaseFaction> {
    private final Map<String, DatabaseFaction> factions = new HashMap<>();
    @Override
    public @NotNull Faction create(@NotNull String display, @NotNull Player owner) {
        return new DatabaseFaction(display, owner);
    }

    @Override
    public @NotNull Faction load(@NotNull String registry) throws FactionNotInStorage {
        return null;
    }

    @Override
    public boolean isLoaded(@NotNull String registry) {
        return false;
    }

    @Override
    public boolean exists(@NotNull String registry) {
        return false;
    }

    @Override
    public @NotNull Map<String, DatabaseFaction> getLoadedFactions() {
        return factions;
    }
}
