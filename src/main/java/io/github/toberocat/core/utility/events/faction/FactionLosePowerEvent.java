package io.github.toberocat.core.utility.events.faction;

import io.github.toberocat.core.factions.Faction;

public class FactionLosePowerEvent extends FactionEvent {

    private final int amount;

    public FactionLosePowerEvent(Faction faction, int amount) {
        super(faction);
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
}
