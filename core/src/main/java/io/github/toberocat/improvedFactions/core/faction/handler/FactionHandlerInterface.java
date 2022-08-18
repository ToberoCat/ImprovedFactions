package io.github.toberocat.improvedFactions.core.faction.handler;

import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionAlreadyExistsException;
import io.github.toberocat.improvedFactions.core.exceptions.faction.IllegalFactionNamingException;
import io.github.toberocat.improvedFactions.core.faction.components.rank.members.FactionRank;
import io.github.toberocat.improvedFactions.core.sender.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.sender.player.OfflineFactionPlayer;
import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.stream.Stream;

public interface FactionHandlerInterface<F extends Faction<F>> {
    @NotNull F create(@NotNull String display, @NotNull FactionPlayer<?> owner) throws IllegalFactionNamingException, FactionAlreadyExistsException;

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

    /**
     * The faction cache is responsible for quick access of factions for players.
     * But if the faction gets deleted, this cache needs to get removed, else it will
     * wrongly display commands and crash the system trying to load the not existing faction
     *
     * @param player The player that should get the faction cache removed
     */
    void removeFactionCache(@NotNull FactionPlayer<?> player);
}
