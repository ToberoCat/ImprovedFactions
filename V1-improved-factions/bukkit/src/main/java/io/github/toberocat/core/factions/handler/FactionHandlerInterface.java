package io.github.toberocat.core.factions.handler;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.components.rank.Rank;
import io.github.toberocat.core.utility.exceptions.faction.FactionNotInStorage;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface FactionHandlerInterface<F extends Faction<F>> {
    @NotNull F create(@NotNull String display, @NotNull Player owner);
    @NotNull F load(@NotNull String registry) throws FactionNotInStorage;
    boolean isLoaded(@NotNull String registry);
    boolean exists(@NotNull String registry);
    @NotNull Map<String, F> getLoadedFactions();
    void deleteCache(@NotNull String registry);
    @NotNull Rank getSavedRank(@NotNull OfflinePlayer player);
}
