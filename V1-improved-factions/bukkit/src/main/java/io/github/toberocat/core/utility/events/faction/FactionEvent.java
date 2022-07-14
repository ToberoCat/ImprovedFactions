package io.github.toberocat.core.utility.events.faction;

import io.github.toberocat.core.factions.local.LocalFaction;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public abstract class FactionEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    protected LocalFaction faction;

    public FactionEvent(LocalFaction faction) {
        this.faction = faction;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public LocalFaction getFaction() {
        return faction;
    }

    public void setFaction(LocalFaction faction) {
        this.faction = faction;
    }
}
