package io.github.toberocat.core.utility.events.faction;

import io.github.toberocat.core.utility.factions.Faction;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class FactionKickEvent extends FactionEventCancelledable {

    protected OfflinePlayer kicked;

    public FactionKickEvent(Faction faction, OfflinePlayer kicked) {
        super(faction);
        this.kicked = kicked;
    }

    public OfflinePlayer getKicked() {
        return kicked;
    }

    public void setKicked(OfflinePlayer kicked) {
        this.kicked = kicked;
    }
}
