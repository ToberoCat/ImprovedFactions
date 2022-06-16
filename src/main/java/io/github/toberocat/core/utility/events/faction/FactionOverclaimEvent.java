package io.github.toberocat.core.utility.events.faction;

import io.github.toberocat.core.factions.Faction;
import org.bukkit.Chunk;

public class FactionOverclaimEvent extends FactionEvent {
    private final Chunk chunk;

    public FactionOverclaimEvent(Faction faction, Chunk chunk) {
        super(faction);
        this.chunk = chunk;
    }
}
