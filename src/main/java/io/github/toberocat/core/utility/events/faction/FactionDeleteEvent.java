package io.github.toberocat.core.utility.events.faction;

import io.github.toberocat.core.utility.factions.Faction;
import org.bukkit.entity.Player;

public class FactionDeleteEvent extends FactionEventCancelledable {

    public FactionDeleteEvent(Faction faction) {
        super(faction);
    }
}
