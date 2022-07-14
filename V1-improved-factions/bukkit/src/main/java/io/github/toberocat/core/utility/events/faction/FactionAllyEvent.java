package io.github.toberocat.core.utility.events.faction;

import io.github.toberocat.core.factions.local.LocalFaction;
import org.jetbrains.annotations.NotNull;

public class FactionAllyEvent extends FactionEvent {

    private final LocalFaction allied;

    public FactionAllyEvent(@NotNull LocalFaction inviter, @NotNull LocalFaction allied) {
        super(inviter);
        this.allied = allied;
    }

    public LocalFaction getAllied() {
        return allied;
    }
}
