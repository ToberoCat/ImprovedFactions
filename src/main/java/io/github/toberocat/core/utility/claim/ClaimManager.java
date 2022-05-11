package io.github.toberocat.core.utility.claim;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.FactionUtility;
import io.github.toberocat.core.utility.Result;
import io.github.toberocat.core.utility.async.AsyncTask;
import io.github.toberocat.core.utility.data.DataAccess;
import io.github.toberocat.core.utility.data.PersistentDataUtility;
import io.github.toberocat.core.utility.dynamic.loaders.DynamicLoader;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.awt.*;
import java.util.List;
import java.util.*;

public class ClaimManager extends DynamicLoader<Player, Player> {

    public static final String SAFEZONE_REGISTRY = "__glb:safezone__";
    public static final String WARZONE_REGISTRY = "__glb:warzone__";
    public static final String UNCLAIMABLE_REGISTRY = "__glb:unclaimable__";

    public static final String UNCLAIMED_CHUNK_REGISTRY = "NONE";

    public final Map<String, ArrayList<Claim>> CLAIMS;

    public ClaimManager() {
        CLAIMS = new HashMap<>();

        for (String world : DataAccess.listFiles("Chunks")) {
            Claim[] claims = DataAccess.getFile("Chunks", world, Claim[].class);
            if (claims == null) continue;

            List<Claim> rawTargetList = Arrays.asList(claims);

            CLAIMS.put(world, new ArrayList<>(rawTargetList));
        }

        for (World world : Bukkit.getWorlds()) {
            if (!CLAIMS.containsKey(world.getName())) {
                CLAIMS.put(world.getName(), new ArrayList<>());
            }
        }

        Subscribe(this);
    }

    public static boolean isManageableZone(String registry) {
        return registry.equals(WARZONE_REGISTRY) || registry.equals(SAFEZONE_REGISTRY);
    }

    @Override
    protected void Disable() {
        for (String world : CLAIMS.keySet()) {
            DataAccess.addFile("Chunks", world, CLAIMS.get(world).toArray(Claim[]::new));
        }

        CLAIMS.clear();
    }

    @Override
    protected void Enable() {

    }

    public Result claimChunk(Faction faction, Chunk chunk) {
        Result result = protectChunk(faction.getRegistryName(), chunk);

        if (result.isSuccess()) faction.setClaimedChunks(faction.getClaimedChunks() + 1);

        return result;
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

        CLAIMS.get(chunk.getWorld().getName()).add(new Claim(chunk.getX(), chunk.getZ(), registry));
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
            return new Result(true);
        }
        String claimRegistry = PersistentDataUtility.read(PersistentDataUtility.FACTION_CLAIMED_KEY,
                PersistentDataType.STRING,
                chunk.getPersistentDataContainer());

        AsyncTask.run(() -> {
            if (claimRegistry == null) return;
            Faction faction = FactionUtility.getFactionByRegistry(claimRegistry);
            if (faction == null) return;

            faction.setClaimedChunks(faction.getClaimedChunks() - 1);
            CLAIMS.get(chunk.getWorld().getName()).removeIf(x -> x.getX() == chunk.getX() && x.getY() == chunk.getZ());
        });

        PersistentDataUtility.write(PersistentDataUtility.FACTION_CLAIMED_KEY,
                PersistentDataType.STRING,
                UNCLAIMED_CHUNK_REGISTRY,
                chunk.getPersistentDataContainer());

        return new Result<String>(true).setPaired(claimRegistry);
    }

    @Override
    protected void loadPlayer(Player value) {

    }

    @Override
    protected void unloadPlayer(Player value) {

    }
}
