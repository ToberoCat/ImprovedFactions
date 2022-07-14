package io.github.toberocat.core.utility.events.faction;

import io.github.toberocat.core.factions.local.LocalFaction;

public class FactionDeleteEvent extends FactionEventCancelledable {

    public FactionDeleteEvent(LocalFaction faction) {
        super(faction);
    }
}
