package io.github.toberocat.core.utility.claim;

import io.github.toberocat.core.utility.async.AsyncCore;
import io.github.toberocat.core.utility.data.DataAccess;
import io.github.toberocat.core.utility.data.PersistentDataUtility;
import io.github.toberocat.core.utility.dynamic.loaders.DynamicLoader;
import io.github.toberocat.core.utility.factions.Faction;
import io.github.toberocat.core.utility.Result;
import io.github.toberocat.core.utility.factions.FactionUtility;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.awt.*;
import java.util.*;
import java.util.List;

public class ClaimManager extends DynamicLoader<Player, Player> {

    public static final String SAFEZONE_REGISTRY = "__glb:safezone__";
    public static final String WARZONE_REGISTRY = "__glb:warzone__";
    public static final String UNCLAIMABLE_REGISTRY = "__glb:unclaimable__";

    public static final String UNCLAIMED_CHUNK_REGISTRY = "NONE";

    public final Map<String, ArrayList<Dimension>> CLAIMS;

    public ClaimManager() {
        CLAIMS = new HashMap<>();

        for (String world : DataAccess.listFiles("Chunks")) {
            Dimension[] claims = DataAccess.getFile("Chunks", world, Dimension[].class);
            if (claims == null) continue;

            List<Dimension> rawTargetList = Arrays.asList(claims);

            CLAIMS.put(world, new ArrayList<>(rawTargetList));
        }

        for (World world : Bukkit.getWorlds()) {
            if (!CLAIMS.containsKey(world.getName())) {
                CLAIMS.put(world.getName(), new ArrayList<>());
            }
        }

        Subscribe(this);
    }

    @Override
    protected void Disable() {
        for (String world : CLAIMS.keySet()) {
            DataAccess.addFile("Chunks", world, CLAIMS.get(world).toArray(Dimension[]::new));
        }

        CLAIMS.clear();
    }

    @Override
    protected void Enable() {

    }

    public Result claimChunk(Faction faction, Chunk chunk) {
        faction.setClaimedChunks(faction.getClaimedChunks() + 1);
        return protectChunk(faction.getRegistryName(), chunk);
    }

    public Result protectChunk(String registry, Chunk chunk) {
        String claimed = PersistentDataUtility.read(PersistentDataUtility.FACTION_CLAIMED_KEY,
                PersistentDataType.STRING, chunk.getPersistentDataContainer());
        if (claimed != null && !claimed.equals(UNCLAIMED_CHUNK_REGISTRY)) {
            return new Result(false).setMessages("CHUNK_ALREADY_PROTECTED", "&cThe chunk you want to claim got already claimed");
        }

        PersistentDataUtility.write(PersistentDataUtility.FACTION_CLAIMED_KEY,
                PersistentDataType.STRING,
                registry,
                chunk.getPersistentDataContainer());

        CLAIMS.get(chunk.getWorld().getName()).add(new Dimension(chunk.getX(), chunk.getZ()));
        return new Result(true);
    }

    public String getFactionRegistry(Chunk chunk) {
        return PersistentDataUtility.read(PersistentDataUtility.FACTION_CLAIMED_KEY,
                PersistentDataType.STRING,
                chunk.getPersistentDataContainer());
    }

    public Result<String> removeProtection(Chunk chunk) {
        if (!PersistentDataUtility.has(PersistentDataUtility.FACTION_CLAIMED_KEY,
                PersistentDataType.STRING, chunk.getPersistentDataContainer())) {
            return  new Result(true);
        }
        String claimRegistry = PersistentDataUtility.read(PersistentDataUtility.FACTION_CLAIMED_KEY,
                PersistentDataType.STRING,
                chunk.getPersistentDataContainer());

        AsyncCore.Run(() -> {
            if (claimRegistry == null) return;
            Faction faction = FactionUtility.getFactionByRegistry(claimRegistry);
            if (faction == null) return;

            faction.setClaimedChunks(faction.getClaimedChunks() - 1);
        });

        PersistentDataUtility.write(PersistentDataUtility.FACTION_CLAIMED_KEY,
                PersistentDataType.STRING,
                UNCLAIMED_CHUNK_REGISTRY,
                chunk.getPersistentDataContainer());

        CLAIMS.get(chunk.getWorld().getName()).remove(new Dimension(chunk.getX(), chunk.getZ()));
        return new Result<String>(true).setPaired(claimRegistry);
    }

    @Override
    protected void loadPlayer(Player value) {

    }

    @Override
    protected void unloadPlayer(Player value) {

    }
}
