package io.github.toberocat.core.utility.events.faction;

import io.github.toberocat.core.factions.Faction;
import org.bukkit.OfflinePlayer;

public class FactionUpdateMemberRankEvent extends FactionEvent {

    private final OfflinePlayer player;
    private final String rank;

    public FactionUpdateMemberRankEvent(Faction faction, OfflinePlayer player, String rank) {
        super(faction);
        this.player = player;
        this.rank = rank;
    }

    public OfflinePlayer getPlayer() {
        return player;
    }

    public String getRank() {
        return rank;
    }
}
