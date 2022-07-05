package io.github.toberocat.improvedfactions.event;

import io.github.toberocat.improvedfactions.factions.Faction;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public abstract class FactionEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    protected boolean isCancelled;

    protected Faction faction;

    public FactionEvent(Faction faction) {
        this.faction = faction;
        isCancelled = false;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }


    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Faction getFaction() {
        return faction;
    }

    public void setFaction(Faction faction) {
        this.faction = faction;
    }
}
