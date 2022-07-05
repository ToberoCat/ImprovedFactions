package io.github.toberocat.improvedfactions.listeners;

import io.github.toberocat.improvedfactions.ImprovedFactionsMain;
import io.github.toberocat.improvedfactions.commands.factionCommands.adminSubCommands.ByPassSubCommand;
import io.github.toberocat.improvedfactions.data.PlayerData;
import io.github.toberocat.improvedfactions.factions.Faction;
import io.github.toberocat.improvedfactions.factions.FactionUtils;
import io.github.toberocat.improvedfactions.utility.ChunkUtils;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class ArrowHitListener implements Listener {

    public void ArrayHit(ProjectileHitEvent event) {
        if (ByPassSubCommand.BYPASS.contains(event.getHitEntity().getUniqueId()) ||
                ByPassSubCommand.BYPASS.contains(event.getHitEntity().getUniqueId())) return;
            if (event.getEntity() instanceof Arrow) {
            if (!ImprovedFactionsMain.getPlugin().getConfig().getBoolean("general.allowClaimProtection")) return;
            if (event.getHitEntity() != null && event.getHitEntity() instanceof Player) {
                PlayerData playerData = ImprovedFactionsMain.playerData.get(event.getHitEntity().getUniqueId());
                if (event.getEntity() instanceof Player) {
                    Player target = (Player) event.getEntity();
                    PlayerData targetData = ImprovedFactionsMain.playerData.get(target.getUniqueId());
                    if (targetData.playerFaction == playerData.playerFaction) {
                        event.setCancelled(true);
                    }
                } else {
                    Faction claimFaction = ChunkUtils.GetFactionClaimedChunk(event.getEntity().getLocation().getChunk());
                        if (claimFaction == null)
                            return;

                        if (FactionUtils.getFaction((Player) event.getHitEntity()) == null) {
                            event.setCancelled(true);
                            return;
                        }

                        if (!claimFaction.getRegistryName()
                                .equals(playerData.playerFaction.getRegistryName())) {
                            event.setCancelled(true);
                        }
                }
            }
        }
    }
}
