package io.github.toberocat.improvedfactions.listeners;

import io.github.toberocat.improvedfactions.ImprovedFactionsMain;
import io.github.toberocat.improvedfactions.commands.factionCommands.adminSubCommands.ByPassSubCommand;
import io.github.toberocat.improvedfactions.data.PlayerData;
import io.github.toberocat.improvedfactions.factions.Faction;
import io.github.toberocat.improvedfactions.utility.ChunkUtils;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OnBlockPlace implements Listener {

    public static Map<Location, UUID> TNT_PLACES = new HashMap<>();

    @EventHandler
    public void OnPlace(BlockPlaceEvent event) {
        if (!ImprovedFactionsMain.getPlugin().getConfig().getBoolean("general.allowClaimProtection")) return;
        PlayerData playerData = ImprovedFactionsMain.playerData.get(event.getPlayer().getUniqueId());
        if (ByPassSubCommand.BYPASS.contains(event.getPlayer().getUniqueId())) return;

        TNT_PLACES.putIfAbsent(event.getBlock().getLocation(), event.getPlayer().getUniqueId());

        Faction claimFaction = ChunkUtils.GetFactionClaimedChunk(event.getBlock().getChunk());
        if (claimFaction == null)
            return;

        if (playerData.playerFaction == null) {
            event.setCancelled(true);
        } else if (!claimFaction.hasPermission(event.getPlayer(), Faction.BUILD_PERMISSION)) {
            event.setCancelled(true);
        }
    }
}
