package io.github.toberocat.improvedfactions.listeners;

import io.github.toberocat.improvedfactions.ImprovedFactionsMain;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnLeave implements Listener {

    @EventHandler
    public void Leave(PlayerQuitEvent event) {
        ImprovedFactionsMain.playerData.remove(event.getPlayer().getUniqueId());
    }
}
