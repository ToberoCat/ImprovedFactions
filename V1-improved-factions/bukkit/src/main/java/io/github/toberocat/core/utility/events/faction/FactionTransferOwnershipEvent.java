package io.github.toberocat.core.utility.events.faction;

import io.github.toberocat.core.factions.Faction;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FactionTransferOwnershipEvent extends FactionEvent {
    private final OfflinePlayer currentOwner;
    private final OfflinePlayer newOwner;

    public FactionTransferOwnershipEvent(@NotNull Faction faction,
                                         @NotNull OfflinePlayer currentOwner,
                                         @NotNull OfflinePlayer newOwner) {
        super(faction);
        this.currentOwner = currentOwner;
        this.newOwner = newOwner;
    }

    public OfflinePlayer getCurrentOwner() {
        return currentOwner;
    }

    public OfflinePlayer getNewOwner() {
        return newOwner;
    }
}
