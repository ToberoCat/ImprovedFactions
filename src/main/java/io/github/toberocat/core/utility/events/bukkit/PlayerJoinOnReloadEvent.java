package io.github.toberocat.core.utility.events.bukkit;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerJoinOnReloadEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;

    public PlayerJoinOnReloadEvent(Player player) {
        this.player = player;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Player getPlayer() {
        return player;
    }

}
