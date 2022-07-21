package io.github.toberocat.core.utility.claim;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.FactionManager;
import io.github.toberocat.core.utility.Result;
import io.github.toberocat.core.utility.async.AsyncTask;
import io.github.toberocat.core.utility.config.DataManager;
import io.github.toberocat.core.utility.data.PersistentDataUtility;
import io.github.toberocat.core.utility.data.access.FileAccess;
import io.github.toberocat.core.utility.events.faction.FactionOverclaimEvent;
import io.github.toberocat.core.utility.events.faction.claim.ChunkProtectEvent;
import io.github.toberocat.core.utility.events.faction.claim.ChunkRemoveProtectionEvent;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.logging.Level;
import java.util.stream.Stream;

public class ClaimManager {

    public static final String SAFEZONE_REGISTRY = "__glb:safezone__";
    public static final String WARZONE_REGISTRY = "__glb:warzone__";
    public static final String UNCLAIMABLE_REGISTRY = "__glb:unclaimable__";

    public static final String UNCLAIMED_CHUNK_REGISTRY = "NONE";

    public static final Map<String, WorldClaims> CLAIMS = new HashMap<>();

    public static void loadChunks() {
        CLAIMS.clear();

        for (String world : FileAccess.listFilesFolder("Chunks")) {
            WorldClaims claims = FileAccess.get("Chunks", world, WorldClaims.class);
            if (claims == null) continue;

            CLAIMS.put(world, claims);
        }

        for (World world : Bukkit.getWorlds()) {
            if (!CLAIMS.containsKey(world.getName())) {
                CLAIMS.put(world.getName(), new WorldClaims());
            }
        }
    }

    public static void dispose() {
        for (String world : CLAIMS.keySet())
            FileAccess.write("Chunks", world, CLAIMS.get(world));

        CLAIMS.clear();
    }


    public static int getRegistryColor(@NotNull String registry) {
        return switch (registry) {
            case SAFEZONE_REGISTRY -> 0x00bfff;
            case WARZONE_REGISTRY -> 0xb30000;
            case UNCLAIMABLE_REGISTRY -> 0x88858e;
            default -> throw new IllegalArgumentException("Registry didn't fit any color");
        };
    }

    public static ArrayList<String> getZones(boolean includeUnclaimable) {
        ArrayList<String> zones = new ArrayList<>(List.of(SAFEZONE_REGISTRY, WARZONE_REGISTRY));
        if (includeUnclaimable) zones.add(UNCLAIMABLE_REGISTRY);

        return zones;
    }

    public static String getDisplay(@NotNull String registry) {
        return switch (registry) {
            case SAFEZONE_REGISTRY -> "territory.safezone";
            case WARZONE_REGISTRY -> "territory.warzone";
            default -> "";
        };
    }

    public static void migrate() {
        NamespacedKey persistentData = new NamespacedKey(MainIF.getIF(), "faction-claimed");

        DataManager chunks = new DataManager(MainIF.getIF(), "Data/chunkData.yml");
        for (String raw : chunks.getConfig().getStringList("claimedChunks")) {
            int x = Integer.parseInt(raw.split(" ")[0]);
            int z = Integer.parseInt(raw.split(" ")[1]);

            String registry = null;
            Chunk chunk = null;
            for (World world : Bukkit.getWorlds()) {
                chunk = world.getChunkAt(x, z);
                registry = chunk.getPersistentDataContainer().get(persistentData, PersistentDataType.STRING);
                if (registry != null) break;
            }

            if (registry == null) continue;

            if (registry.equals("safezone")) registry = SAFEZONE_REGISTRY;

            protectChunk(registry, chunk);
        }

        MainIF.logMessage(Level.INFO, "Migrated every chunk. You can now delete the chunkData.yml file in Data folder safely without worrying, if no warnings / errors appear above");
    }

    public static boolean isManageableZone(String registry) {
        return WARZONE_REGISTRY.equals(registry) || SAFEZONE_REGISTRY.equals(registry) ||
                UNCLAIMABLE_REGISTRY.equals(registry);
    }

    public static Stream<Claim> registryClaims(String registry) {
        return CLAIMS.values()
                .stream()
                .flatMap(Collection::stream)
                .filter(x -> x.getRegistry().equals(registry));
    }

    public static Stream<Claim> registryClaims(String registry, String world) {
        if (!CLAIMS.containsKey(world))
            throw new IllegalArgumentException("The world you gave wasn't represented in the claims list");
        return CLAIMS.get(world).stream().filter(x -> x.getRegistry().equals(registry));
    }

    public static Result<?> claimChunk(Faction faction, Chunk chunk) {
        String registry = getFactionRegistry(chunk);
        if (registry != null && !isManageableZone(registry)) {
            Faction claim = FactionManager.getFactionByRegistry(registry);
            int power = claim.getPowerManager().getCurrentPower();
            int claims = claim.getClaimedChunks();

            if (Boolean.TRUE.equals(ConfigManager
                    .getValue("general.limit-chunks-to-power")) && claims >= power) return Result
                    .failure("NO_CLAIM_POWER",
                            "&cYou don't have enough power to claim this chunk1");

            if (power > claims) return Result.failure("CHUNK_ALREADY_PROTECTED",
                    "&cThe chunk you want to claim got already claimed");

            if (!isCorner(chunk)) return Result.failure("CHUNK_NO_CORNER",
                    "&cThe chunk isn't a corner, so you can't overclaim it");
            removeClaim(faction, chunk);
            AsyncTask.runSync(() ->
                    Bukkit.getPluginManager().callEvent(new FactionOverclaimEvent(claim, faction, chunk)));
        }

        Result<?> result = protectChunk(faction.getRegistryName(), chunk);
        if (!result.isSuccess()) return result;

        faction.getPowerManager().addClaimedChunk();

        return result;
    }

    private static boolean isCorner(Chunk chunk) {
        Chunk[] neighbours = getNeighbourChunks(chunk);
        for (Chunk neighbour : neighbours) {
            String registry = getFactionRegistry(neighbour);
            if (registry == null || registry.startsWith("__glb:")) return true;
        }
        return false;
    }

    private static Chunk[] getNeighbourChunks(Chunk chunk) {
        Chunk[] neighbours = new Chunk[4];
        int centerX = chunk.getX();
        int centerZ = chunk.getZ();

        neighbours[0] = chunk.getWorld().getChunkAt(centerX - 1, centerZ);
        neighbours[2] = chunk.getWorld().getChunkAt(centerX + 1, centerZ);

        neighbours[1] = chunk.getWorld().getChunkAt(centerX, centerZ - 1);
        neighbours[3] = chunk.getWorld().getChunkAt(centerX, centerZ + 1);

        return neighbours;
    }

    public static Result<?> protectChunk(String registry, Chunk chunk) {
        String claimed = PersistentDataUtility.read(PersistentDataUtility.FACTION_CLAIMED_KEY,
                PersistentDataType.STRING, chunk.getPersistentDataContainer());
        if (claimed != null && !claimed.equals(UNCLAIMED_CHUNK_REGISTRY)) {
            return new Result<>(false).setMessages("CHUNK_ALREADY_PROTECTED",
                    "&cThe chunk you want to claim got already claimed");
        }

        PersistentDataUtility.write(PersistentDataUtility.FACTION_CLAIMED_KEY,
                PersistentDataType.STRING,
                registry,
                chunk.getPersistentDataContainer());

        String worldName = chunk.getWorld().getName();
        if (!CLAIMS.containsKey(worldName)) CLAIMS.put(worldName, new WorldClaims());

        CLAIMS.get(worldName).add(new Claim(chunk.getX(), chunk.getZ(), registry));
        AsyncTask.runSync(() -> Bukkit.getPluginManager()
                .callEvent(new ChunkProtectEvent(registry, chunk)));
        return new Result<>(true);
    }

    public static String getFactionRegistry(Chunk chunk) {
        return PersistentDataUtility.read(PersistentDataUtility.FACTION_CLAIMED_KEY,
                PersistentDataType.STRING,
                chunk.getPersistentDataContainer());
    }

    public static Result<String> removeProtection(Chunk chunk) {
        if (!PersistentDataUtility.has(PersistentDataUtility.FACTION_CLAIMED_KEY,
                PersistentDataType.STRING, chunk.getPersistentDataContainer())) {
            return new Result<>(true);
        }
        String claimRegistry = PersistentDataUtility.read(PersistentDataUtility.FACTION_CLAIMED_KEY,
                PersistentDataType.STRING,
                chunk.getPersistentDataContainer());

        if (claimRegistry != null) {
            Faction faction = FactionManager.getFactionByRegistry(claimRegistry);
            if (faction != null) {
                faction.setClaimedChunks(faction.getClaimedChunks() - 1);

                String worldName = chunk.getWorld().getName();
                if (!CLAIMS.containsKey(worldName)) CLAIMS.put(worldName, new WorldClaims());
                CLAIMS.get(worldName).removeIf(x -> x.getX() == chunk.getX() && x.getY() == chunk.getZ());
            }
        }

        PersistentDataUtility.remove(PersistentDataUtility.FACTION_CLAIMED_KEY, chunk.getPersistentDataContainer());
        AsyncTask.runSync(() -> Bukkit.getPluginManager()
                .callEvent(new ChunkRemoveProtectionEvent(claimRegistry, chunk)));
        return new Result<String>(true).setPaired(claimRegistry);
    }

    public static Result<String> removeClaim(Faction faction, Chunk chunk) {
        Result<String> result = removeProtection(chunk);
        if (!result.isSuccess()) return result;

        faction.getPowerManager().removeClaimedChunk();

        return result;
    }
}