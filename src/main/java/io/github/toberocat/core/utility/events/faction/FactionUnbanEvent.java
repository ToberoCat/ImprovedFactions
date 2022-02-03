package io.github.toberocat.core.utility.events.faction;

import io.github.toberocat.core.utility.factions.Faction;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class FactionUnbanEvent extends FactionEventCancelledable {

    protected OfflinePlayer banned;

    public FactionUnbanEvent(Faction faction, OfflinePlayer banned) {
        super(faction);
        this.banned = banned;
    }

    public OfflinePlayer getBanned() {
        return banned;
    }

    public void setBanned(OfflinePlayer banned) {
        this.banned = banned;
    }
}
