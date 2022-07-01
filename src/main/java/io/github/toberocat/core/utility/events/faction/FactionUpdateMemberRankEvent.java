package io.github.toberocat.core.utility.events.faction;

import io.github.toberocat.core.factions.Faction;
import org.bukkit.OfflinePlayer;

public class FactionUpdateMemberRankEvent extends FactionEvent {

    private final OfflinePlayer player;
    private final String newRank, oldRank;

    public FactionUpdateMemberRankEvent(Faction faction, OfflinePlayer player, String oldRank, String newRank) {
        super(faction);
        this.player = player;
        this.oldRank = oldRank;
        this.newRank = newRank;
    }

    public OfflinePlayer getPlayer() {
        return player;
    }

    public String getNewRank() {
        return newRank;
    }

    public String getOldRank() {
        return oldRank;
    }
}
