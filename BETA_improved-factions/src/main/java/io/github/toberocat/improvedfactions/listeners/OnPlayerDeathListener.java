package io.github.toberocat.improvedfactions.listeners;

import io.github.toberocat.improvedfactions.ImprovedFactionsMain;
import io.github.toberocat.improvedfactions.data.PlayerData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class OnPlayerDeathListener implements Listener {

    @EventHandler
    public void OnDeath(PlayerDeathEvent event) {
        PlayerData playerData = ImprovedFactionsMain.playerData.get(event.getEntity().getUniqueId());

        if (playerData.playerFaction == null) return;

        playerData.playerFaction.getPowerManager().playerDeath(event.getEntity());
    }
}
