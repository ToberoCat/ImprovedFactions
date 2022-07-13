package io.github.toberocat.core.utility.events.faction;

import io.github.toberocat.core.factions.Faction;

public class FactionLoadEvent extends FactionEvent {
    public FactionLoadEvent(Faction faction) {
        super(faction);
    }
}
