package io.github.toberocat.improvedfactions.data;

import io.github.toberocat.improvedfactions.factions.Faction;
import org.bukkit.Chunk;

import java.util.ArrayList;
import java.util.List;

public class PlayerData {

    public Display display;
    public ChunkData chunkData;

    public Faction playerFaction;
    public List<String> invitations;

    public PlayerData() {
        display = new Display();
        chunkData = new ChunkData();
        invitations = new ArrayList<>();
    }

    public class Display {
        public boolean alreadyDisplayedRegion = false;
    }

    public class ChunkData {
        public boolean isInClaimedChunk = false;
        public String factionRegistry = "";
    }
}

