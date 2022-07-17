package io.github.toberocat.core.utility.events.faction;

import io.github.toberocat.core.factions.Faction;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class FactionTransferOwnershipEvent extends FactionEvent {
    private final Player currentOwner;
    private final OfflinePlayer newOwner;

    public FactionTransferOwnershipEvent(Faction faction, Player currentOwner, OfflinePlayer newOwner) {
        super(faction);
        this.currentOwner = currentOwner;
        this.newOwner = newOwner;
    }

    public Player getCurrentOwner() {
        return currentOwner;
    }

    public OfflinePlayer getNewOwner() {
        return newOwner;
    }
}
