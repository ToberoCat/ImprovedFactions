package io.github.toberocat.improvedfactions.listener;

import io.github.toberocat.MainIF;
import io.github.toberocat.improvedfactions.CacheCoreExtension;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    @EventHandler
    public void breakBlock(BlockBreakEvent event) {
        final Block block = event.getBlock();
        final String registry = MainIF.getIF().getClaimManager().getFactionRegistry(block.getChunk());
        CacheCoreExtension.meshCache.updateBlockCache(registry, block);
    }

    @EventHandler
    public void placeBlock(BlockPlaceEvent event) {
        final Block block = event.getBlock();
        final String registry = MainIF.getIF().getClaimManager().getFactionRegistry(block.getChunk());
        CacheCoreExtension.meshCache.updateBlockCache(registry, block);
    }
}
