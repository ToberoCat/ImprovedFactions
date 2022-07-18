package io.github.toberocat.core.factions.handler;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.database.DatabaseFactionHandler;
import io.github.toberocat.core.factions.local.LocalFactionHandler;
import io.github.toberocat.core.utility.exceptions.faction.FactionNotInStorage;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * This class provides an interface for handling all low level faction interactions.
 * This needs to be done to make ut easier for users to interact with factions
 * without worrying about the different back ends (Like the different methods & handling ways of Local and Database factions)
 */
public abstract class FactionHandler {
    private static final FactionHandlerInterface<?> handler = createInterface();

    public FactionHandler() {
    }

    public static @NotNull FactionHandlerInterface<?> createInterface() {
        if (MainIF.config().getBoolean("sql.useSql", false)) return new DatabaseFactionHandler();
        else return new LocalFactionHandler();
    }

    public static @NotNull Faction createFaction(@NotNull String display,
                                             @NotNull Player owner) {
        return handler.create(display, owner);
    }

    public static boolean isLoaded(@NotNull String registry) {
        return handler.isLoaded(registry);
    }

    public boolean exists(@NotNull String registry) {
        return handler.exists(registry);
    }

    public @NotNull Map<String, ?> getLoadedFactions() {
        return handler.getLoadedFactions();
    }

    public static @NotNull Faction loadFromStorage(@NotNull String registry) throws FactionNotInStorage {
        return handler.load(registry);
    }
}
