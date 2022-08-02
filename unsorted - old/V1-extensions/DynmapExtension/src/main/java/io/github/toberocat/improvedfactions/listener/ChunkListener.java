package io.github.toberocat.improvedfactions.listener;


import io.github.toberocat.core.utility.events.faction.claim.ChunkProtectEvent;
import io.github.toberocat.core.utility.events.faction.claim.ChunkRemoveProtectionEvent;
import io.github.toberocat.improvedfactions.DynmapExtension;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public record ChunkListener(DynmapExtension extension) implements Listener {

    @EventHandler
    public void claim(ChunkProtectEvent event) {
        String registry = event.getRegistry();
        Chunk chunk = event.getChunk();
        if (registry == null || chunk == null) return;

        extension.claim(chunk, registry);
    }

    @EventHandler
    public void unclaim(ChunkRemoveProtectionEvent event) {
        Chunk chunk = event.getChunk();
        if (chunk == null) return;

        extension.handle.unclaim(chunk);
    }
}
