package io.github.toberocat.core.utility.events.faction;

import io.github.toberocat.core.factions.Faction;
import org.bukkit.entity.Player;

public class FactionMemberOnlineEvent extends FactionEvent {

    private final Player player;

    public FactionMemberOnlineEvent(Faction faction, Player player) {
        super(faction);
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
