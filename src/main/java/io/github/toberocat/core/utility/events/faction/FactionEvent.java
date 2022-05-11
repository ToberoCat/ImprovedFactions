package io.github.toberocat.core.utility.events.faction;

import io.github.toberocat.core.factions.Faction;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public abstract class FactionEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    protected Faction faction;

    public FactionEvent(Faction faction) {
        this.faction = faction;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public Faction getFaction() {
        return faction;
    }

    public void setFaction(Faction faction) {
        this.faction = faction;
    }
}
