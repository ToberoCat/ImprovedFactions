package io.github.toberocat.core.listeners.world;

import io.github.toberocat.core.factions.handler.FactionHandler;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.claim.ClaimManager;
import io.github.toberocat.core.utility.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.core.utility.exceptions.setting.SettingNotFoundException;
import io.github.toberocat.core.utility.settings.type.BoolSetting;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.jetbrains.annotations.NotNull;

public class ExplodeListener implements Listener {

    @EventHandler
    public void explode(BlockExplodeEvent event) {
        if (Utility.isDisabled(event.getBlock().getWorld())) return;

        for (Block block : event.blockList()) {
            try {
                handle(block, event);
            } catch (FactionNotInStorage | SettingNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void handle(@NotNull Block block, BlockExplodeEvent event) throws FactionNotInStorage, SettingNotFoundException {
        String registry = ClaimManager.getChunkRegistry(block.getChunk());
        if (registry == null) return;
        if (!((BoolSetting) FactionHandler.getFaction(registry).getSetting("explosions")).getSelected())
            event.setCancelled(true);
    }
}
