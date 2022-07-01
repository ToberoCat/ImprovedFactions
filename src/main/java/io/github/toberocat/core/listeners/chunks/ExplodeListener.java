package io.github.toberocat.core.listeners.chunks;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.factions.FactionUtility;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.settings.type.BoolSetting;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;

public class ExplodeListener implements Listener {

    @EventHandler
    public void Explode(BlockExplodeEvent event) {
        if (Utility.isDisabled(event.getBlock().getWorld())) return;

        for (Block block : event.blockList()) {
            String registry = MainIF.getIF().getClaimManager().getFactionRegistry(block.getChunk());
            if (registry == null) continue;
            if (!((BoolSetting)FactionUtility.getFactionByRegistry(registry).getFactionPerm().getFactionSettings().get("explosions")).getSelected()) {
                event.setCancelled(true);
                return;
            }
        }
    }
}
