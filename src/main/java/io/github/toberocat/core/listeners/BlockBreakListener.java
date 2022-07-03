package io.github.toberocat.core.listeners;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.commands.admin.AdminBypassSubCommand;
import io.github.toberocat.core.debug.Debugger;
import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.FactionUtility;
import io.github.toberocat.core.factions.permission.FactionPerm;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.claim.ClaimManager;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void Break(BlockBreakEvent event) {
        if (AdminBypassSubCommand.BYPASSING.contains(event.getPlayer().getUniqueId())) return;
        if (Utility.isDisabled(event.getPlayer().getWorld())) return;

        ClaimManager claimManager = MainIF.getIF().getClaimManager();
        Chunk blockChunk = event.getBlock().getChunk();

        String claim = claimManager.getFactionRegistry(blockChunk);
        if (claim == null) return; // Chunk isn't protected
        if (MainIF.getIF().isStandby() || !MainIF.getIF().isEnabled()) {
            Language.sendRawMessage("Factions is in standby. Protection is enabled for claimed chunk", event.getPlayer());

            event.setCancelled(true);
            event.setDropItems(false);
            event.getBlock().getDrops().clear();

            return;
        }
        if (ClaimManager.isManageableZone(claim)) {

            event.setCancelled(true);
            event.setDropItems(false);
            event.getBlock().getDrops().clear();

            return;
        }
        if (!FactionUtility.doesFactionExist(claim)) return;

        Faction claimFaction = FactionUtility.getFactionByRegistry(claim);
        if (claimFaction == null) {
            Language.sendRawMessage("You have encountered a problem with improved factions! Go ahead " +
                    "and tell the admins about the save shutdown. Error: BlockBreak wasn't able to find required faction", event.getPlayer());
            Debugger.logWarning("BlockBreakListener.java wasn't able to find claimfaction. Entering savemode.\nInfo: " +
                    "Player: &e" + event.getPlayer().getName());
            MainIF.getIF().saveShutdown("Wasn't able to find faction that claimed chunk &6"
                    + blockChunk.getX() + ", " + blockChunk.getZ());

            event.setCancelled(true);
            event.setDropItems(false);
            event.getBlock().getDrops().clear();

            return;
        }

        if (!claimFaction.hasPermission(event.getPlayer(), FactionPerm.BREAK_PERM)) {
            event.setCancelled(true);
            event.setDropItems(false);
            event.getBlock().getDrops().clear();
        }
    }
}
