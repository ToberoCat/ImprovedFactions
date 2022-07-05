package io.github.toberocat.improvedfactions.events;

import io.github.toberocat.improvedfactions.mesh.WorldCache;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class WorldCacheCreatedEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    private final @NotNull WorldCache cache;

    public WorldCacheCreatedEvent(@NotNull WorldCache cache) {
        this.cache = cache;
    }

    public @NotNull WorldCache getCache() {
        return cache;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
