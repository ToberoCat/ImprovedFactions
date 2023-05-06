package io.github.toberocat.improvedFactions.core.faction.handler;

import io.github.toberocat.improvedFactions.core.exceptions.faction.*;
import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.faction.database.mysql.MySqlFactionHandler;
import io.github.toberocat.improvedFactions.core.faction.local.LocalFactionHandler;
import io.github.toberocat.improvedFactions.core.handler.ConfigFile;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * This class provides an interface for handling all low level faction interactions.
 * This needs to be done to make ut easier for users to interact with factions
 * without worrying about the different back ends (Like the different methods & handling ways of Local and Database factions)
 */
public abstract class FactionHandler {
    private static final @NotNull FactionHandlerInterface<?> handler = createInterface();

    private static @NotNull FactionHandlerInterface<?> createInterface() {
        if (ConfigFile.api().getBool("storage.use-mysql", false)) return new MySqlFactionHandler();
        else return new LocalFactionHandler();
    }

    public static @NotNull Faction<?> createFaction(@NotNull String display,
                                                    @NotNull FactionPlayer<?> owner)
            throws IllegalFactionNamingException, FactionAlreadyExistsException,
            FactionIsFrozenException, PlayerIsAlreadyInFactionException, PlayerIsBannedException {
        return handler.create(display, owner);
    }

    public static boolean isLoaded(@NotNull String registry) {
        return handler.isLoaded(registry);
    }

    public static @NotNull Faction<?> loadFromStorage(@NotNull String registry) throws FactionNotInStorage {
        return handler.load(registry);
    }

    public static void unload(@NotNull String registry) throws FactionNotInStorage {
        Faction<?> faction = getFaction(registry);
        if (faction.getMembers()
                .filter(uuid -> Objects.nonNull(ImprovedFactions.api().getPlayer(uuid)))
                .count() > 1) return;

        FactionHandler.unload(registry);
    }

    public static void deleteCache(@NotNull String registry) {
        handler.deleteCache(registry);
    }

    public static void deleteFromFile(@NotNull String registry) {
        handler.deleteFromFile(registry);
    }

    public static boolean exists(@NotNull String registry) {
        return handler.exists(registry);
    }

    public static @NotNull Map<String, Faction<?>> getLoadedFactions() {
        return (Map<String, Faction<?>>) handler.getLoadedFactions();
    }

    public static @NotNull Faction<?> getFaction(@NotNull String registry) throws FactionNotInStorage {
        return handler.getFaction(registry);
    }

    public static @NotNull Stream<String> getAllFactions() {
        return handler.getAllFactions();
    }

    public static void dispose() {
        new HashMap<>(FactionHandler.getLoadedFactions())
                .keySet()
                .forEach(FactionHandler::deleteCache);
    }
}
