package io.github.toberocat.core.listeners.world;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.commands.admin.AdminBypassSubCommand;
import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.handler.FactionHandler;
import io.github.toberocat.core.factions.local.managers.FactionPerm;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.claim.ClaimManager;
import io.github.toberocat.core.utility.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.core.utility.exceptions.setting.SettingNotFoundException;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void breakBlock(BlockBreakEvent event) {
        if (AdminBypassSubCommand.BYPASSING.contains(event.getPlayer().getUniqueId())) return;
        if (Utility.isDisabled(event.getPlayer().getWorld())) return;

        Chunk blockChunk = event.getBlock().getChunk();

        String claim = ClaimManager.getChunkRegistry(blockChunk);
        if (claim == null) return; // Chunk isn't protected
        if (MainIF.getIF().isStandby() || !MainIF.getIF().isEnabled()) {
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
        if (!FactionHandler.exists(claim)) return;

        try {
            Faction<?> faction = FactionHandler.getFaction(claim);
            if (faction.hasPermission(event.getPlayer(), FactionPerm.BREAK_PERM)) return;

            event.setCancelled(true);
            event.setDropItems(false);
            event.getBlock().getDrops().clear();
        } catch (FactionNotInStorage | SettingNotFoundException ignored) {
        }
    }
}
