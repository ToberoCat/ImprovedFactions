package io.github.toberocat.improvedfactions.event.chunk;

import io.github.toberocat.improvedfactions.factions.Faction;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class OnChunkEnterEvent extends Event implements Cancellable {

    private boolean isCancelled = false;
    private static final HandlerList HANDLERS = new HandlerList();

    private Player player;
    private Chunk chunk;

    public OnChunkEnterEvent(Player player, Chunk chunk) {
        this.player = player;
        this.chunk = chunk;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public Player getPlayer() {
        return player;
    }
    public void setPlayer(Player player) {
        this.player = player;
    }

    public Chunk getChunk() {
        return chunk;
    }
}

