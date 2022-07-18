package io.github.toberocat.core.factions.handler;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.utility.exceptions.faction.FactionNotInStorage;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface FactionHandlerInterface<F extends Faction> {
    @NotNull Faction create(@NotNull String display, @NotNull Player owner);
    @NotNull Faction load(@NotNull String registry) throws FactionNotInStorage;
    boolean isLoaded(@NotNull String registry);
    boolean exists(@NotNull String registry);
    @NotNull Map<String, F> getLoadedFactions();
}
