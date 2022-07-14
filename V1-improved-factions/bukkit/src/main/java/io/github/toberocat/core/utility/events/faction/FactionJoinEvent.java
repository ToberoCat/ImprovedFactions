package io.github.toberocat.core.utility.events.faction;

import io.github.toberocat.core.factions.local.LocalFaction;
import org.bukkit.entity.Player;

public class FactionJoinEvent extends FactionEventCancelledable {

    protected Player player;

    public FactionJoinEvent(LocalFaction faction, Player player) {
        super(faction);
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
