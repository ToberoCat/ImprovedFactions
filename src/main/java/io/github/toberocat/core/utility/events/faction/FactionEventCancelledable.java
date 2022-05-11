package io.github.toberocat.core.utility.events.faction;

import io.github.toberocat.core.factions.Faction;
import org.bukkit.event.Cancellable;

public abstract class FactionEventCancelledable extends FactionEvent implements Cancellable {

    protected boolean isCancelled;

    public FactionEventCancelledable(Faction faction) {
        super(faction);
        isCancelled = false;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }
}
