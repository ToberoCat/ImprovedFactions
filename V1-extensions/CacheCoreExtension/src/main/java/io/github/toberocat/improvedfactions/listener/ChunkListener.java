package io.github.toberocat.improvedfactions.listener;


import io.github.toberocat.core.utility.events.faction.claim.ChunkProtectEvent;
import io.github.toberocat.core.utility.events.faction.claim.ChunkRemoveProtectionEvent;
import io.github.toberocat.improvedfactions.CacheCoreExtension;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChunkListener implements Listener {
    @EventHandler
    public void claimChunk(ChunkProtectEvent event) {
        Chunk chunk = event.getChunk();
        String registry = event.getRegistry();

        CacheCoreExtension.meshCache.cacheChunk(registry, chunk);
    }

    @EventHandler
    public void removeChunk(ChunkRemoveProtectionEvent event) {
        Chunk chunk = event.getChunk();
        String registry = event.getRegistry();

        CacheCoreExtension.meshCache.unloadCacheChunk(registry, chunk);
    }
}
