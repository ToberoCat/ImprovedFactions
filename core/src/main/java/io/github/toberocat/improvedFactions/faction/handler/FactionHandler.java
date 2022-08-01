package io.github.toberocat.improvedFactions.faction.handler;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.components.rank.Rank;
import io.github.toberocat.core.factions.database.DatabaseFactionHandler;
import io.github.toberocat.core.factions.local.LocalFactionHandler;
import io.github.toberocat.core.utility.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.core.utility.exceptions.faction.PlayerHasNoFactionException;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        if (MainIF.config().getBoolean("sql.useSql", false)) return new DatabaseFactionHandler();
        else return new LocalFactionHandler();
    }

    public static @NotNull Faction<?> createFaction(@NotNull String display,
                                                    @NotNull Player owner) {
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
                .filter(uuid -> Objects.nonNull(Bukkit.getPlayer(uuid)))
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

    public static @NotNull Faction<?> getFaction(@NotNull Player player)
            throws PlayerHasNoFactionException, FactionNotInStorage {
        String registry = handler.getPlayerFaction(player);
        if (registry == null) throw new PlayerHasNoFactionException(player);

        return getFaction(registry);
    }

    public static @NotNull Faction<?> getFaction(@NotNull OfflinePlayer player)
            throws PlayerHasNoFactionException, FactionNotInStorage {
        String registry = handler.getPlayerFaction(player);
        if (registry == null) throw new PlayerHasNoFactionException(player);

        return getFaction(registry);
    }


    public static @Nullable String getPlayerFaction(@NotNull OfflinePlayer player) {
        return handler.getPlayerFaction(player);
    }

    public static @Nullable String getPlayerFaction(@NotNull Player player) {
        return handler.getPlayerFaction(player);
    }


    public static boolean isInFaction(@NotNull OfflinePlayer player) {
        return handler.isInFaction(player);
    }

    public static boolean isInFaction(@NotNull Player player) {
        return handler.isInFaction(player);
    }

    public static @NotNull Stream<String> getAllFactions() {
        return handler.getAllFactions();
    }

    public static void removeFactionCache(@NotNull Player player) {
        handler.removeFactionCache(player);
    }

    public static void dispose() {
        new HashMap<>(FactionHandler.getLoadedFactions())
                .keySet()
                .forEach(FactionHandler::deleteCache);
    }

    /**
     * Gets the rank that's being saved for a player. No processing is done with this rank yet.
     * It can only be A faction rank (Owner, Admin, Moderator, Elder, Member) and a Guest (No faction)
     * <p>
     * Note: Ally relation is being ignored!
     *
     * @param player The player you ant to get the rank from
     * @return The raw rank of the player
     */
    public static @NotNull Rank getSavedRank(@NotNull OfflinePlayer player) {
        return handler.getSavedRank(player);
    }

}
