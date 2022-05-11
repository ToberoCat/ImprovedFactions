package io.github.toberocat.core.listeners;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.commands.admin.AdminBypassSubCommand;
import io.github.toberocat.core.debug.Debugger;
import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.FactionUtility;
import io.github.toberocat.core.factions.permission.FactionPerm;
import io.github.toberocat.core.utility.claim.ClaimManager;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityInteractEvent;

public class InteractListener implements Listener {

    @EventHandler
    public void Interaction(EntityInteractEvent event) {
        if (AdminBypassSubCommand.BYPASSING.contains(event.getEntity().getUniqueId())) return;

        ClaimManager claimManager = MainIF.getIF().getClaimManager();
        Chunk blockChunk = event.getBlock().getChunk();

        String claim = claimManager.getFactionRegistry(blockChunk);
        if (claim == null) return; // Chunk isn't protected
        if (MainIF.getIF().isStandby() || !MainIF.getIF().isEnabled()) {
            if (event.getEntity() instanceof Player player) Language.sendRawMessage("Factions is in standby." +
                    " Protection is enabled for claimed chunk", player);
            event.setCancelled(true);
            return;
        }

        if (ClaimManager.isManageableZone(claim)) {
            event.setCancelled(true);
            return;
        }
        if (!FactionUtility.doesFactionExist(claim)) return;

        Faction claimFaction = FactionUtility.getFactionByRegistry(claim);
        if (claimFaction == null) {
            if (event.getEntity() instanceof Player player) Language.sendRawMessage("You have encountered a problem" +
                    " with improved factions! Go ahead and tell the admins about the save shutdown. " +
                    "Error: BlockPlace wasn't able to find required faction", player);
            Debugger.logWarning("BlockPlaceListener.java wasn't able to find claimfaction. Entering savemode.\nInfo: " +
                    "Entity: &e" + event.getEntity().getName());
            MainIF.getIF().saveShutdown("Wasn't able to find faction that claimed chunk &6"
                    + blockChunk.getX() + ", " + blockChunk.getZ());
            event.setCancelled(true);
            return;
        }

        if (event.getEntity() instanceof Player player && !claimFaction.hasPermission(player, FactionPerm.INTERACT_PERM))
            event.setCancelled(true);
    }
}
