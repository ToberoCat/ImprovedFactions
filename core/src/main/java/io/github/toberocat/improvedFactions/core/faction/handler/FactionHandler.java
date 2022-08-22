package io.github.toberocat.improvedFactions.core.faction.handler;

import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionAlreadyExistsException;
import io.github.toberocat.improvedFactions.core.exceptions.faction.IllegalFactionNamingException;
import io.github.toberocat.improvedFactions.core.faction.components.rank.members.FactionRank;
import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.sender.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.sender.player.OfflineFactionPlayer;
import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.faction.database.mysql.MySqlFactionHandler;
import io.github.toberocat.improvedFactions.core.faction.local.LocalFactionHandler;
import io.github.toberocat.improvedFactions.core.handler.ConfigHandler;
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
        if (ConfigHandler.api().getBool("storage.use-mysql", false)) return new MySqlFactionHandler();
        else return new LocalFactionHandler();
    }

    public static @NotNull Faction<?> createFaction(@NotNull String display,
                                                    @NotNull FactionPlayer<?> owner)
            throws IllegalFactionNamingException, FactionAlreadyExistsException {
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

    /**
     * Gets the rank that's being saved for a player. No post-processing got done with this rank yet.
     * It can only be a faction rank (Owner, Admin, Moderator, Elder, Member) and a Guest (No faction)
     * <p>
     * Note: Ally relation is being ignored!
     *
     * @param player The player you ant to get the rank from
     * @return The raw rank of the player
     */
    public static @NotNull FactionRank getSavedRank(@NotNull OfflineFactionPlayer<?> player) {
        return handler.getSavedRank(player);
    }

}
