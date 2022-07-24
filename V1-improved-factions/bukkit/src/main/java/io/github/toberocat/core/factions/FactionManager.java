package io.github.toberocat.core.factions;

import io.github.toberocat.core.factions.handler.FactionHandler;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.exceptions.faction.FactionNotInStorage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

/**
 * This class is responsible for handling the faction loading & unloading for RAM savage
 */
public class FactionManager implements Listener {

    @EventHandler
    private void join(@NotNull PlayerJoinEvent event) {
        String registry = FactionHandler.getPlayerFaction(event.getPlayer());
        if (registry == null) return;

        try {
            FactionHandler.getFaction(registry);
        } catch (FactionNotInStorage e) {
            Utility.except(e);
        }
    }

    @EventHandler
    private void leave(@NotNull PlayerQuitEvent event) {
        String registry = FactionHandler.getPlayerFaction(event.getPlayer());
        if (registry == null) return;

        try {
            FactionHandler.unload(registry);
        } catch (FactionNotInStorage e) {
            Utility.except(e);
        }
    }
}
