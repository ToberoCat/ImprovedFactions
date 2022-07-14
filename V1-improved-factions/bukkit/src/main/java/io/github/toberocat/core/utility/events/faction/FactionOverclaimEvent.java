package io.github.toberocat.core.utility.events.faction;

import io.github.toberocat.core.factions.local.LocalFaction;
import org.bukkit.Chunk;

public class FactionOverclaimEvent extends FactionEvent {
    private final Chunk chunk;
    private final LocalFaction newOwners;

    public FactionOverclaimEvent(LocalFaction original, LocalFaction newOwner, Chunk chunk) {
        super(original);
        this.newOwners = newOwner;
        this.chunk = chunk;
    }

    public LocalFaction getNewOwners() {
        return newOwners;
    }

    public Chunk getChunk() {
        return chunk;
    }
}
