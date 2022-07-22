package io.github.toberocat.core.factions;

import io.github.toberocat.core.factions.handler.FactionHandler;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.exceptions.faction.FactionNotInStorage;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * This class is responsible for handling the faction loading & unloading for RAM savage
 */
public class FactionManager implements Listener {

    public static void unloadUseless(@NotNull String registry) throws FactionNotInStorage {
        Faction<?> faction = getFactionByRegistry(registry);
        if (faction.getMembers()
                .filter(uuid -> Objects.nonNull(Bukkit.getPlayer(uuid)))
                .count() > 1) return;

        FactionHandler.unload(registry);
    }

    public static @NotNull Faction<?> getFactionByRegistry(@NotNull String registry) throws FactionNotInStorage {
        if (FactionHandler.getLoadedFactions().containsKey(registry))
            return FactionHandler.getLoadedFactions().get(registry);

        return FactionHandler.loadFromStorage(registry);
    }

    @EventHandler
    private void join(@NotNull PlayerJoinEvent event) {
        String registry = FactionHandler.getPlayerFactionRegistry(event.getPlayer());
        if (registry == null) return;

        try {
            getFactionByRegistry(registry);
        } catch (FactionNotInStorage e) {
            Utility.except(e);
        }
    }

    @EventHandler
    private void leave(@NotNull PlayerQuitEvent event) {
        String registry = FactionHandler.getPlayerFactionRegistry(event.getPlayer());
        if (registry == null) return;

        try {
            unloadUseless(registry);
        } catch (FactionNotInStorage e) {
            Utility.except(e);
        }
    }
}
