package io.github.toberocat.core.listeners.player;

import io.github.toberocat.core.player.PlayerSettings;
import io.github.toberocat.core.utility.tips.TipOfTheDay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveListener implements Listener {

    @EventHandler
    public void OnLeave(PlayerQuitEvent event) {
        PlayerSettings.PlayerLeave(event.getPlayer().getUniqueId());
        PlayerJoinListener.PLAYER_JOINS.remove(event.getPlayer().getUniqueId());
        TipOfTheDay.resetPlayer(event.getPlayer());
    }
}
