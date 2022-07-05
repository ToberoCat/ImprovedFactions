package io.github.toberocat.improvedfactions.listeners;

import io.github.toberocat.improvedfactions.commands.factionCommands.adminSubCommands.ByPassSubCommand;
import io.github.toberocat.improvedfactions.factions.Faction;
import io.github.toberocat.improvedfactions.factions.FactionMember;
import io.github.toberocat.improvedfactions.factions.FactionUtils;
import io.github.toberocat.improvedfactions.utility.ChunkUtils;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.UUID;

public class BlockExplosionListener implements Listener {

    @EventHandler
    public void EntityExplode(EntityExplodeEvent event) {
        UUID explosionPlaces = OnBlockPlace.TNT_PLACES.get(event.getEntity().getLocation());
        if (ByPassSubCommand.BYPASS.contains(explosionPlaces)) return;
        String registry = null;
        if (explosionPlaces != null) {
            Faction f = FactionUtils.getFaction(explosionPlaces);
            registry = f.getRegistryName();
        }
        for (Block block : event.blockList()) {
            Faction bFaction = ChunkUtils.GetFactionClaimedChunk(block.getChunk());
            if (registry != null && bFaction != null && bFaction.getRelationManager().getEnemies().contains(registry)) {
                event.setCancelled(false);
            }
            if (bFaction != null) {
                event.setCancelled(true);
                return;
            }
        }
    }
}
