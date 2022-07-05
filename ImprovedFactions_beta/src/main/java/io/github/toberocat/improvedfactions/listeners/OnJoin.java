package io.github.toberocat.improvedfactions.listeners;

import io.github.toberocat.improvedfactions.ImprovedFactionsMain;
import io.github.toberocat.improvedfactions.data.PlayerData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnJoin implements Listener {

    @EventHandler
    public void Join(PlayerJoinEvent event) {
        ImprovedFactionsMain.AddPlayerData(event.getPlayer());
        ImprovedFactionsMain.getPlugin().getPlayerMessages().ReceiveMessages(event.getPlayer());
    }
}
