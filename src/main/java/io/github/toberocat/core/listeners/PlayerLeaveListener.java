package io.github.toberocat.core.listeners;

import io.github.toberocat.core.utility.factions.FactionUtility;
import io.github.toberocat.core.utility.factions.Faction;
import io.github.toberocat.core.utility.settings.PlayerSettings;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveListener implements Listener {

    @EventHandler
    public void OnLeave(PlayerQuitEvent event) {
        Faction faction = FactionUtility.getPlayerFaction(event.getPlayer());
        PlayerSettings.PlayerLeave(event.getPlayer().getUniqueId());
        PlayerJoinListener.PLAYER_JOINS.remove(event.getPlayer().getUniqueId());
    }
}
