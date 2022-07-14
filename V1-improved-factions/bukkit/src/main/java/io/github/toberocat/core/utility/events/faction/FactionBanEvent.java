package io.github.toberocat.core.utility.events.faction;

import io.github.toberocat.core.factions.local.LocalFaction;
import org.bukkit.OfflinePlayer;

public class FactionBanEvent extends FactionEventCancelledable {

    protected OfflinePlayer banned;

    public FactionBanEvent(LocalFaction faction, OfflinePlayer banned) {
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
