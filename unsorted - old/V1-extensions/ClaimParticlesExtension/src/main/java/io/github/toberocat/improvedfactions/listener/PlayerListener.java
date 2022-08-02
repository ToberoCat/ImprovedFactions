package io.github.toberocat.improvedfactions.listener;

import io.github.toberocat.improvedfactions.ClaimParticleExtension;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    @EventHandler
    public void join(PlayerJoinEvent event) {
        ClaimParticleExtension.handler.addPlayer(event.getPlayer());
    }

    @EventHandler
    public void quit(PlayerQuitEvent event) {
        ClaimParticleExtension.handler.removePlayer(event.getPlayer());
    }
}
