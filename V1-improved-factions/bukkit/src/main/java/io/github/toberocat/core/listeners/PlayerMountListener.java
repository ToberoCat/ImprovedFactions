package io.github.toberocat.core.listeners;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.FactionManager;
import io.github.toberocat.core.factions.local.managers.FactionPerm;
import io.github.toberocat.core.utility.Utility;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.spigotmc.event.entity.EntityMountEvent;

public class PlayerMountListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void mount(EntityMountEvent event) {
        if (Utility.isDisabled(event.getEntity().getWorld())) return;

        if (event.getEntity() instanceof Player player) {
            Chunk chunk = player.getLocation().getChunk();
            String registry = MainIF.getIF().getClaimManager().getFactionRegistry(chunk);
            if (registry == null) return;
            Faction faction = FactionManager.getFactionByRegistry(registry);
            if (!faction.hasPermission(player, FactionPerm.MOUNT_PERM)) event.setCancelled(true);
        }
    }
}
