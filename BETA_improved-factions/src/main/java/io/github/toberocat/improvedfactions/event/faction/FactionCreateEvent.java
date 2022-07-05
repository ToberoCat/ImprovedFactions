package io.github.toberocat.improvedfactions.event.faction;

import io.github.toberocat.improvedfactions.factions.Faction;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FactionCreateEvent extends Event implements Cancellable {

    private boolean isCancelled = false;
    private static final HandlerList HANDLERS = new HandlerList();

    private Faction faction;
    private Player player;
    private String cancelMessage;

    public FactionCreateEvent(Faction faction, Player player) {
        this.faction = faction;
        this.player = player;
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

    public Faction getFaction() {
        return faction;
    }
    public void setFaction(Faction faction) {
        this.faction = faction;
    }

    public Player getPlayer() {
        return player;
    }
    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getCancelMessage() {
        return cancelMessage;
    }

    public void setCancelMessage(String cancelMessage) {
        this.cancelMessage = cancelMessage;
    }
}
