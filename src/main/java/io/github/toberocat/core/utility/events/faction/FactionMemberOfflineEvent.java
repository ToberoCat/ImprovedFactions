package io.github.toberocat.core.utility.events.faction;

import io.github.toberocat.core.factions.Faction;
import org.bukkit.entity.Player;

public class FactionMemberOfflineEvent extends FactionEvent {

    private final Player player;

    public FactionMemberOfflineEvent(Faction faction, Player player) {
        super(faction);
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}