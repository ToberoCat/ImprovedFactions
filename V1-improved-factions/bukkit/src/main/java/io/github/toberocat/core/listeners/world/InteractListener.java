package io.github.toberocat.core.listeners.world;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.commands.admin.AdminBypassSubCommand;
import io.github.toberocat.core.debug.Debugger;
import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.FactionManager;
import io.github.toberocat.core.factions.local.managers.FactionPerm;
import io.github.toberocat.core.utility.claim.ClaimManager;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void Interaction(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (AdminBypassSubCommand.BYPASSING.contains(player.getUniqueId())) return;

        ClaimManager claimManager = MainIF.getIF().getClaimManager();
        if (event.getClickedBlock() == null) return;

        Chunk blockChunk = event.getClickedBlock().getChunk();

        String claim = claimManager.getChunkRegistry(blockChunk);
        if (claim == null) return; // Chunk isn't protected
        if (MainIF.getIF().isStandby() || !MainIF.getIF().isEnabled()) {
            Language.sendRawMessage("Factions is in standby. Protection is enabled for claimed chunk", player);

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
                    "and tell the admins about the save shutdown. Error: Interaction wasn't able to find required faction", player);
            Debugger.logWarning("InteractionListener.java wasn't able to find claimfaction. Entering savemode.\nInfo: " +
                    "Player: &e" + player.getName());
            MainIF.getIF().saveShutdown("Wasn't able to find faction that claimed chunk &6"
                    + blockChunk.getX() + ", " + blockChunk.getZ());

            event.setCancelled(true);
            return;
        }

        if (!claimFaction.hasPermission(player, FactionPerm.BREAK_PERM))
            event.setCancelled(true);
    }
}
