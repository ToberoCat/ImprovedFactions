package io.github.toberocat.improvedfactions.listeners;

import io.github.toberocat.improvedfactions.ImprovedFactionsMain;
import io.github.toberocat.improvedfactions.commands.factionCommands.adminSubCommands.ByPassSubCommand;
import io.github.toberocat.improvedfactions.data.PlayerData;
import io.github.toberocat.improvedfactions.factions.Faction;
import io.github.toberocat.improvedfactions.factions.FactionUtils;
import io.github.toberocat.improvedfactions.utility.ChunkUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

public class OnInteract implements Listener {

    @EventHandler
    public void Interact(PlayerInteractEvent event) {
        if (!ImprovedFactionsMain.getPlugin().getConfig().getBoolean("general.allowClaimProtection")) return;
        if (ByPassSubCommand.BYPASS.contains(event.getPlayer().getUniqueId())) return;

        PlayerData playerData = ImprovedFactionsMain.playerData.get(event.getPlayer().getUniqueId());
        if (event.getClickedBlock() == null) return;
        Faction claimFaction = ChunkUtils.GetFactionClaimedChunk(event.getClickedBlock().getChunk());
        if (claimFaction == null)
            return;

        if (playerData.playerFaction != null && !claimFaction.getRegistryName()
                .equals(playerData.playerFaction.getRegistryName())) {
            event.setCancelled(true);
        } else if (!claimFaction.hasPermission(event.getPlayer(), Faction.BUILD_PERMISSION)) {
            event.setCancelled(true);
        }
    }
}
