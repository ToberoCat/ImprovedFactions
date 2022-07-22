package io.github.toberocat.core.factions.handler;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.components.rank.Rank;
import io.github.toberocat.core.factions.database.DatabaseFactionHandler;
import io.github.toberocat.core.factions.local.LocalFactionHandler;
import io.github.toberocat.core.utility.data.PersistentDataUtility;
import io.github.toberocat.core.utility.exceptions.faction.FactionNotInStorage;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

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

    public static void unload(@NotNull String registry) {
        handler.unloadFaction(registry);
    }

    public static void deleteCache(@NotNull String registry) {
        handler.deleteCache(registry);
    }

    public static boolean exists(@NotNull String registry) {
        return handler.exists(registry);
    }

    public static <F extends Faction<F>> @NotNull Map<String, F> getLoadedFactions() {
        return (Map<String, F>) handler.getLoadedFactions();
    }

    public static @Nullable String getPlayerFactionRegistry(@NotNull OfflinePlayer player) {
        return handler.getPlayerFaction(player);
    }

    public static @Nullable String getPlayerFactionRegistry(@NotNull Player player) {
        return handler.getPlayerFaction(player);
    }

    public static void removeFactionCache(@NotNull Player player) {
        handler.removeFactionCache(player);
    }

    public static void dispose() {
        Map<String, Faction<?>> copy = new HashMap<>(FactionHandler.getLoadedFactions());
        copy.keySet().forEach(FactionHandler::deleteCache);
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
