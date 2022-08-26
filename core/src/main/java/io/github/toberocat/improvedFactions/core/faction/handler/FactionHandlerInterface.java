package io.github.toberocat.improvedFactions.core.faction.handler;

import io.github.toberocat.improvedFactions.core.exceptions.faction.*;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.sender.player.FactionPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.stream.Stream;

public interface FactionHandlerInterface<F extends Faction<F>> {
    @NotNull F create(@NotNull String display, @NotNull FactionPlayer<?> owner) throws IllegalFactionNamingException, FactionAlreadyExistsException, FactionIsFrozenException, PlayerIsAlreadyInFactionException, PlayerIsBannedException;

    @NotNull F load(@NotNull String registry) throws FactionNotInStorage;

    boolean isLoaded(@NotNull String registry);

    boolean exists(@NotNull String registry);

    @NotNull Map<String, F> getLoadedFactions();

    @NotNull Stream<String> getAllFactions();

    void deleteCache(@NotNull String registry);

    void deleteFromFile(@NotNull String registry);

    default @NotNull Faction<F> getFaction(@NotNull String registry) throws FactionNotInStorage {
        if (FactionHandler.getLoadedFactions().containsKey(registry))
            return getLoadedFactions().get(registry);

        return load(registry);
    }
}
