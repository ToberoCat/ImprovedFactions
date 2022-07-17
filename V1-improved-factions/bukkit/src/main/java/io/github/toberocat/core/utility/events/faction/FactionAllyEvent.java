package io.github.toberocat.core.utility.events.faction;

import io.github.toberocat.core.factions.Faction;
import org.jetbrains.annotations.NotNull;

public class FactionAllyEvent extends FactionEvent {

    private final Faction allied;

    public FactionAllyEvent(@NotNull Faction inviter, @NotNull Faction allied) {
        super(inviter);
        this.allied = allied;
    }

    public Faction getAllied() {
        return allied;
    }
}
