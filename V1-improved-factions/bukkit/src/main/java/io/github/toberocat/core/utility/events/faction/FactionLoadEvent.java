package io.github.toberocat.core.utility.events.faction;

import io.github.toberocat.core.factions.local.LocalFaction;

public class FactionLoadEvent extends FactionEvent {
    public FactionLoadEvent(LocalFaction faction) {
        super(faction);
    }
}
