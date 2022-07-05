package io.github.toberocat.core.utility.events.faction;

import io.github.toberocat.core.factions.Faction;

public class FactionDeleteEvent extends FactionEventCancelledable {

    public FactionDeleteEvent(Faction faction) {
        super(faction);
    }
}
