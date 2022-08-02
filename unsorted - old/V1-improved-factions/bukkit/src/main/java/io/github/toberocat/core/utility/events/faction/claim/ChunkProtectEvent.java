package io.github.toberocat.core.utility.events.faction.claim;

import org.bukkit.Chunk;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ChunkProtectEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Chunk chunk;
    private final String registry;

    public ChunkProtectEvent(String registry, Chunk chunk) {
        this.chunk = chunk;
        this.registry = registry;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public Chunk getChunk() {
        return chunk;
    }

    public String getRegistry() {
        return registry;
    }
}
