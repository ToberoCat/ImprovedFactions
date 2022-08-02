package io.github.toberocat.core.utility.dynamic.loaders;

import io.github.toberocat.core.utility.events.bukkit.PlayerJoinOnReloadEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public abstract class PlayerJoinLoader extends DynamicLoader<Player, Player> {
    @EventHandler
    public void OnPlayerJoin(PlayerJoinEvent event) {
        loadPlayer(event.getPlayer());
    }

    @EventHandler
    public void OnPlayerReloadJoin(PlayerJoinOnReloadEvent event) {
        loadPlayer(event.getPlayer());
    }

    @EventHandler
    public void OnPlayerQuit(PlayerQuitEvent event) {
        unloadPlayer(event.getPlayer());
    }

    @Override
    protected void Disable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            unloadPlayer(player);
        }
    }

    @Override
    protected void Enable() {

    }
}
