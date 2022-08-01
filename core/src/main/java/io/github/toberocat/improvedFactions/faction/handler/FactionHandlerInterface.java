package io.github.toberocat.improvedFactions.faction.handler;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.components.rank.Rank;
import io.github.toberocat.core.utility.exceptions.faction.FactionNotInStorage;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.stream.Stream;

public interface FactionHandlerInterface<F extends Faction<F>> {
    @NotNull F create(@NotNull String display, @NotNull Player owner);
    @NotNull F load(@NotNull String registry) throws FactionNotInStorage;

    boolean isLoaded(@NotNull String registry);
    boolean exists(@NotNull String registry);

    @NotNull Map<String, F> getLoadedFactions();
    @NotNull Stream<String> getAllFactions();

    void deleteCache(@NotNull String registry);

    @NotNull Rank getSavedRank(@NotNull OfflinePlayer player);

    default @NotNull Faction<F> getFaction(@NotNull String registry) throws FactionNotInStorage {
        if (FactionHandler.getLoadedFactions().containsKey(registry))
            return getLoadedFactions().get(registry);

        return load(registry);
    }

    @Nullable String getPlayerFaction(@NotNull OfflinePlayer player);
    @Nullable String getPlayerFaction(@NotNull Player player);
    boolean isInFaction(@NotNull OfflinePlayer player);
    boolean isInFaction(@NotNull Player player);

    /**
     * The faction cache is responsible for quick access of factions for players.
     * But if the faction gets deleted, this cache needs to get removed, else it will
     * wrongly display commands and crash the system trying to load the not existing faction
     *
     * @param player The player that should get the faction cache removed
     */
    void removeFactionCache(@NotNull Player player);
}
