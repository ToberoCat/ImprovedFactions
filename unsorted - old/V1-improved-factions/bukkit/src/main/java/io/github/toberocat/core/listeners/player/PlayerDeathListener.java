package io.github.toberocat.core.listeners.player;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.FactionManager;
import io.github.toberocat.core.utility.Utility;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void death(PlayerDeathEvent event) {
        if (Utility.isDisabled(event.getEntity().getWorld())) return;

        Faction faction = FactionManager.getPlayerFaction(event.getEntity());
        if (faction == null) return;

        faction.getPowerManager().death();
    }
}
