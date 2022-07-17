package io.github.toberocat.core.utility.events.faction;

import io.github.toberocat.core.factions.Faction;
import org.bukkit.Chunk;

public class FactionOverclaimEvent extends FactionEvent {
    private final Chunk chunk;
    private final Faction newOwners;

    public FactionOverclaimEvent(Faction original, Faction newOwner, Chunk chunk) {
        super(original);
        this.newOwners = newOwner;
        this.chunk = chunk;
    }

    public Faction getNewOwners() {
        return newOwners;
    }

    public Chunk getChunk() {
        return chunk;
    }
}
