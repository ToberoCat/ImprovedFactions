package io.github.toberocat.core.utility.claim;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.FactionManager;
import io.github.toberocat.core.utility.Result;
import io.github.toberocat.core.utility.async.AsyncTask;
import io.github.toberocat.core.utility.claim.component.Claim;
import io.github.toberocat.core.utility.claim.component.WorldClaims;
import io.github.toberocat.core.utility.claim.component.database.DatabaseWorldClaims;
import io.github.toberocat.core.utility.claim.component.local.LocalWorldClaims;
import io.github.toberocat.core.utility.data.PersistentDataUtility;
import io.github.toberocat.core.utility.data.access.AbstractAccess;
import io.github.toberocat.core.utility.data.database.DatabaseAccess;
import io.github.toberocat.core.utility.events.faction.FactionOverclaimEvent;
import io.github.toberocat.core.utility.events.faction.claim.ChunkProtectEvent;
import io.github.toberocat.core.utility.events.faction.claim.ChunkRemoveProtectionEvent;
import io.github.toberocat.core.utility.exceptions.chunks.ChunkAlreadyClaimedException;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class ClaimManager {

    public static final String SAFEZONE_REGISTRY = "__glb:safezone__";
    public static final String WARZONE_REGISTRY = "__glb:warzone__";
    public static final String UNCLAIMABLE_REGISTRY = "__glb:unclaimable__";

    public static final String UNCLAIMED_CHUNK_REGISTRY = "NONE";

    public static final Map<String, WorldClaims> CLAIMS = new HashMap<>();

    public static void loadChunks() {
        CLAIMS.clear();


        Bukkit.getWorlds().forEach(ClaimManager::createWorldClaim);
    }

    private static void createWorldClaim(@NotNull World world) {
        String worldName = world.getName();
        CLAIMS.put(worldName, AbstractAccess.isAccess(DatabaseAccess.class) ?
                DatabaseWorldClaims.loadWorldClaim(worldName) :
                LocalWorldClaims.loadWorldClaim(worldName));
    }

    public static void dispose() {
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

    public static @Nullable String getZoneDisplay(@NotNull String registry) {
        return switch (registry) {
            case SAFEZONE_REGISTRY -> "territory.safezone";
            case WARZONE_REGISTRY -> "territory.warzone";
            default -> null;
        };
    }

    public static boolean isManageableZone(String registry) {
        return WARZONE_REGISTRY.equals(registry) || SAFEZONE_REGISTRY.equals(registry) ||
                UNCLAIMABLE_REGISTRY.equals(registry);
    }

    public static Stream<Claim> registryClaims(String registry) {
        return CLAIMS.values()
                .stream()
                .flatMap(WorldClaims::getClaims)
                .filter(x -> x.getRegistry().equals(registry));
    }

    public static Stream<Claim> registryClaims(String registry, String world) {
        if (!CLAIMS.containsKey(world))
            throw new IllegalArgumentException("The world you gave wasn't represented in the claims list");
        return CLAIMS.get(world).getClaims().filter(x -> x.getRegistry().equals(registry));
    }


    public static void protectChunk(@NotNull String registry, @NotNull Chunk chunk)
            throws ChunkAlreadyClaimedException {
        String claimed = PersistentDataUtility.read(PersistentDataUtility.FACTION_CLAIMED_KEY,
                PersistentDataType.STRING, chunk.getPersistentDataContainer());

        if (claimed != null && !claimed.equals(UNCLAIMED_CHUNK_REGISTRY))
            throw new ChunkAlreadyClaimedException(claimed);

        PersistentDataUtility.write(PersistentDataUtility.FACTION_CLAIMED_KEY,
                PersistentDataType.STRING,
                registry,
                chunk.getPersistentDataContainer());

        String worldName = chunk.getWorld().getName();
        if (!CLAIMS.containsKey(worldName)) createWorldClaim(chunk.getWorld());

        CLAIMS.get(worldName).add(new Claim(chunk.getX(), chunk.getZ(), registry, worldName));
        AsyncTask.runSync(() -> Bukkit.getPluginManager()
                .callEvent(new ChunkProtectEvent(registry, chunk)));
    }

    public static void removeProtection(@NotNull Chunk chunk) {
        if (!PersistentDataUtility.has(PersistentDataUtility.FACTION_CLAIMED_KEY,
                PersistentDataType.STRING, chunk.getPersistentDataContainer())) return;

        String claimRegistry = PersistentDataUtility.read(PersistentDataUtility.FACTION_CLAIMED_KEY,
                PersistentDataType.STRING,
                chunk.getPersistentDataContainer());

        PersistentDataUtility.remove(PersistentDataUtility.FACTION_CLAIMED_KEY,
                chunk.getPersistentDataContainer());
        AsyncTask.runSync(() -> Bukkit.getPluginManager()
                .callEvent(new ChunkRemoveProtectionEvent(claimRegistry, chunk)));
        ClaimManager.CLAIMS.get(chunk.getWorld().getName()).remove(chunk.getX(), chunk.getZ());
    }

    public static @Nullable String getChunkRegistry(@NotNull Chunk chunk) {
        return PersistentDataUtility.read(PersistentDataUtility.FACTION_CLAIMED_KEY,
                PersistentDataType.STRING,
                chunk.getPersistentDataContainer());
    }
}
