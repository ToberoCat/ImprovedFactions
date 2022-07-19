package io.github.toberocat.core.listeners;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.commands.admin.AdminBypassSubCommand;
import io.github.toberocat.core.debug.Debugger;
import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.FactionManager;
import io.github.toberocat.core.factions.local.managers.FactionPerm;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.claim.ClaimManager;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Map;

public class BlockPlaceListener implements Listener {

    public static Map<Location, Block> TNT_PLACE_LOCATIONS;

    @EventHandler
    public void Place(BlockPlaceEvent event) {
        if (AdminBypassSubCommand.BYPASSING.contains(event.getPlayer().getUniqueId())) return;
        if (Utility.isDisabled(event.getPlayer().getWorld())) return;

        ClaimManager claimManager = MainIF.getIF().getClaimManager();
        Chunk blockChunk = event.getBlock().getChunk();

        String claim = claimManager.getFactionRegistry(blockChunk);
        if (claim == null) return; // Chunk isn't protected
        if (MainIF.getIF().isStandby() || !MainIF.getIF().isEnabled()) {
            Language.sendRawMessage("Factions is in standby. Protection is enabled for claimed chunk", event.getPlayer());
            event.setCancelled(true);
            return;
        }

        if (ClaimManager.isManageableZone(claim)) {
            event.setCancelled(true);
            return;
        }
        if (!FactionManager.doesFactionExist(claim)) return;

        Faction claimFaction = FactionManager.getFactionByRegistry(claim);
        if (claimFaction == null) {
            Language.sendRawMessage("You have encountered a problem with improved factions! Go ahead " +
                    "and tell the admins about the save shutdown. Error: BlockPlace wasn't able to find required faction", event.getPlayer());
            Debugger.logWarning("BlockPlaceListener.java wasn't able to find claimfaction. Entering savemode.\nInfo: " +
                    "Player: &e" + event.getPlayer().getName());
            MainIF.getIF().saveShutdown("Wasn't able to find faction that claimed chunk &6"
                    + blockChunk.getX() + ", " + blockChunk.getZ());
            event.setCancelled(true);
            return;
        }

        if (!claimFaction.hasPermission(event.getPlayer(), FactionPerm.PLACE_PERM)) event.setCancelled(true);
    }
}
