package io.github.toberocat.core.utility.events.faction.member;

import io.github.toberocat.core.factions.local.LocalFaction;
import io.github.toberocat.core.utility.events.faction.FactionEvent;
import org.bukkit.entity.Player;

public class FactionMemberOnlineEvent extends FactionEvent {

    private final Player player;

    public FactionMemberOnlineEvent(LocalFaction faction, Player player) {
        super(faction);
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
