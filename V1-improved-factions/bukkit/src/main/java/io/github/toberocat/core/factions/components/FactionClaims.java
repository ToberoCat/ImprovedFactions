package io.github.toberocat.core.factions.components;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.handler.FactionHandler;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.claim.ClaimManager;
import io.github.toberocat.core.utility.claim.component.Claim;
import io.github.toberocat.core.utility.exceptions.chunks.ChunkAlreadyClaimedException;
import io.github.toberocat.core.utility.exceptions.faction.FactionNotInStorage;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
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
     *
     * @return The total claims this faction has
     */
    public long getTotal() {
        return claims.values().stream().flatMap(x -> x).count();
    }

    public void claim(@NotNull String worldName, int x, int z) throws ChunkAlreadyClaimedException, FactionNotInStorage {
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
            throws ChunkAlreadyClaimedException, FactionNotInStorage {
        if (faction.getRegistry().equals(claim)) throw new ChunkAlreadyClaimedException(claim);
        if (ClaimManager.isManageableZone(claim)) throw new ChunkAlreadyClaimedException(claim);
        if (checkPower(faction, FactionHandler.getFaction(claim))) return;

        ClaimManager.removeProtection(chunk);
    }

    private boolean checkPower(@NotNull Faction<?> sender, @NotNull Faction<?> target) {
        return sender.getActivePower().compareTo(target.getActiveMaxPower()) > 0;
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

            ClaimManager.removeProtection(world.getChunkAt(x, z));
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
