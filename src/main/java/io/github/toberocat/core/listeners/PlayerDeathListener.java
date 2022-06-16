package io.github.toberocat.core.listeners;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.FactionUtility;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void death(PlayerDeathEvent event) {
        Faction faction = FactionUtility.getPlayerFaction(event.getEntity());
        if (faction == null) return;

        faction.getPowerManager().death();
    }
}
