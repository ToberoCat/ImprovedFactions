package io.github.toberocat.core.factions.components;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.async.AsyncTask;
import io.github.toberocat.core.utility.claim.component.Claim;
import io.github.toberocat.core.utility.claim.ClaimManager;
import io.github.toberocat.core.utility.data.PersistentDataUtility;
import io.github.toberocat.core.utility.events.faction.claim.ChunkRemoveProtectionEvent;
import io.github.toberocat.core.utility.exceptions.chunks.ChunkAlreadyClaimedException;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.stream.Stream;

public record FactionClaims<F extends Faction<F>>(
        LinkedHashMap<String, Stream<FactionClaim<F>>> claims, F faction) {

    public static <F extends Faction<F>> FactionClaims<F> createClaims(@NotNull F faction) {
        LinkedHashMap<String, Stream<FactionClaim<F>>> worldClaims = new LinkedHashMap<>();
        ClaimManager.CLAIMS.forEach((name, claims) -> {
            World world = Bukkit.getWorld(name);
            if (world == null || Utility.isDisabled(world)) return;

            worldClaims.put(name, claims.getClaims()
                    .filter(x -> x != null && x.getRegistry().equals(faction.getRegistry()))
                    .map(x -> FactionClaim.fromClaim(faction, x)));
        });


        return new FactionClaims<>(worldClaims, faction);
    }

    /**
     * This value is only temporary and is not getting updated once the object got created
     * @return The total claims this faction has
     */
    public long getTotal() {
        return claims.values().stream().flatMap(x -> x).count();
    }

    public void claim(@NotNull String worldName, int x, int z) throws ChunkAlreadyClaimedException {
        World world = Bukkit.getWorld(worldName);
        if (world == null) return;

        Chunk chunk = world.getChunkAt(x, z);
        try {
            ClaimManager.protectChunk(faction.getRegistry(), chunk);
        } catch (ChunkAlreadyClaimedException e) {
            overclaim(chunk, faction, e.getRegistry());
        }
    }

    private void overclaim(@NotNull Chunk chunk, @NotNull Faction<?> faction, @NotNull String claim)
            throws ChunkAlreadyClaimedException {
        if (faction.getRegistry().equals(claim)) throw new ChunkAlreadyClaimedException(claim);
        if (ClaimManager.isManageableZone(claim)) throw new ChunkAlreadyClaimedException(claim);
        if (faction)
    }

    public void unclaimAll() {
        claims.forEach((worldName, chunks) -> chunks.forEach(FactionClaim::unclaim));
        claims.clear();
    }

    public record FactionClaim<F extends Faction<F>>(Faction<F> faction, String world, int x, int z) {

        public static <F extends Faction<F>> FactionClaim<F> fromClaim(@NotNull F faction,
                                                                       @NotNull Claim claim) {
            return new FactionClaim<>(faction, claim.getWorld(), claim.getX(), claim.getY());
        }

        public void unclaim() {
            World world = Bukkit.getWorld(world());
            if (world == null) return;

            Chunk chunk = world.getChunkAt(x, z);
            if (!PersistentDataUtility.has(PersistentDataUtility.FACTION_CLAIMED_KEY,
                    PersistentDataType.STRING, chunk.getPersistentDataContainer())) return;

            String claimRegistry = PersistentDataUtility.read(PersistentDataUtility.FACTION_CLAIMED_KEY,
                    PersistentDataType.STRING,
                    chunk.getPersistentDataContainer());

            PersistentDataUtility.remove(PersistentDataUtility.FACTION_CLAIMED_KEY,
                    chunk.getPersistentDataContainer());
            AsyncTask.runSync(() -> Bukkit.getPluginManager()
                    .callEvent(new ChunkRemoveProtectionEvent(claimRegistry, chunk)));
            ClaimManager.CLAIMS.get(world()).remove(x, z);
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, z);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FactionClaim<?> that = (FactionClaim<?>) o;
            return x == that.x && z == that.z;
        }
    }
}
