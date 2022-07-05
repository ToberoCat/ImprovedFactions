package io.github.toberocat.improvedfactions.event.chunk;

import io.github.toberocat.improvedfactions.factions.Faction;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ChunkClaimEvent extends Event {
    private boolean isCancelled = false;
    private static final HandlerList HANDLERS = new HandlerList();

    private Chunk chunk;
    private Faction faction;

    public ChunkClaimEvent(Chunk chunk, Faction faction) {
        this.chunk = chunk;
        this.faction = faction;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Chunk getChunk() {
        return chunk;
    }

    public Faction getFaction() {
        return faction;
    }

    public void setFaction(Faction faction) {
        this.faction = faction;
    }
}
