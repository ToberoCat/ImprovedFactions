package io.github.toberocat.core.utility.events.faction.power;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.utility.events.faction.FactionEvent;

public class FactionPowerEvent extends FactionEvent {

    private final int change;
    private final PowerCause cause;

    public FactionPowerEvent(Faction faction, int change, PowerCause cause) {
        super(faction);
        this.change = change;
        this.cause = cause;
    }

    public int getChange() {
        return change;
    }

    public PowerCause getCause() {
        return cause;
    }
}
