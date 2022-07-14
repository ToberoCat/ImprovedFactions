package io.github.toberocat.core.listeners;

import io.github.toberocat.core.factions.local.LocalFaction;
import io.github.toberocat.core.factions.local.FactionUtility;
import io.github.toberocat.core.utility.Utility;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void death(PlayerDeathEvent event) {
        if (Utility.isDisabled(event.getEntity().getWorld())) return;

        LocalFaction faction = FactionUtility.getPlayerFaction(event.getEntity());
        if (faction == null) return;

        faction.getPowerManager().death();
    }
}
