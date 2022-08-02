package io.github.toberocat.improvedfactions.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FullCacheCreatedEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    public FullCacheCreatedEvent() {

    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
