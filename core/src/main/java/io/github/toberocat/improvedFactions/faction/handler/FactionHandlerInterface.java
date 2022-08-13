package io.github.toberocat.improvedFactions.faction.handler;

import io.github.toberocat.improvedFactions.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.exceptions.faction.IllegalFactionNamingException;
import io.github.toberocat.improvedFactions.faction.Faction;
import io.github.toberocat.improvedFactions.faction.components.rank.Rank;
import io.github.toberocat.improvedFactions.faction.components.rank.members.FactionRank;
import io.github.toberocat.improvedFactions.player.FactionPlayer;
import io.github.toberocat.improvedFactions.player.OfflineFactionPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.stream.Stream;

public interface FactionHandlerInterface<F extends Faction<F>> {
    @NotNull F create(@NotNull String display, @NotNull FactionPlayer<?> owner) throws IllegalFactionNamingException;

    @NotNull F load(@NotNull String registry) throws FactionNotInStorage;

    boolean isLoaded(@NotNull String registry);

    boolean exists(@NotNull String registry);

    @NotNull Map<String, F> getLoadedFactions();

    @NotNull Stream<String> getAllFactions();

    void deleteCache(@NotNull String registry);

    @NotNull FactionRank getSavedRank(@NotNull OfflineFactionPlayer<?> player);

    default @NotNull Faction<F> getFaction(@NotNull String registry) throws FactionNotInStorage {
        if (FactionHandler.getLoadedFactions().containsKey(registry))
            return getLoadedFactions().get(registry);

        return load(registry);
    }

    @Nullable String getPlayerFaction(@NotNull OfflineFactionPlayer<?> player);

    @Nullable String getPlayerFaction(@NotNull FactionPlayer<?> player);

    boolean isInFaction(@NotNull OfflineFactionPlayer<?> player);

    boolean isInFaction(@NotNull FactionPlayer<?> player);

    /**
     * The faction cache is responsible for quick access of factions for players.
     * But if the faction gets deleted, this cache needs to get removed, else it will
     * wrongly display commands and crash the system trying to load the not existing faction
     *
     * @param player The player that should get the faction cache removed
     */
    void removeFactionCache(@NotNull FactionPlayer<?> player);
}
