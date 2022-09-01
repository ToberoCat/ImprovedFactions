package io.github.toberocat.improvedFactions.core.faction.components;

import io.github.toberocat.improvedFactions.core.claims.ClaimHandler;
import io.github.toberocat.improvedFactions.core.claims.component.Claim;
import io.github.toberocat.improvedFactions.core.exceptions.chunk.ChunkAlreadyClaimedException;
import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionDoesntOwnChunkException;
import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.faction.handler.FactionHandler;
import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.utils.CUtils;
import io.github.toberocat.improvedFactions.core.world.Chunk;
import io.github.toberocat.improvedFactions.core.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.stream.Stream;

public final class FactionClaims<F extends Faction<F>> {
    private final F faction;
    private LinkedHashMap<String, Stream<FactionClaim<F>>> claims;

    public FactionClaims(F faction) {
        this.faction = faction;
        this.claims = null;
    }

    public static <F extends Faction<F>> FactionClaims<F> createClaims(@NotNull F faction) {
        return new FactionClaims<>(faction);
    }

    public void forceCalculate() {
        claims = new LinkedHashMap<>();
        ClaimHandler.forEach((name, c) -> {
            World<?> world = ImprovedFactions.api().getWorld(name);
            if (world == null || CUtils.isWorldAllowed(world)) return;

            claims.put(name, c.getAllClaims()
                    .filter(Objects::nonNull)
                    .filter(x -> x.getRegistry().equals(faction.getRegistry()))
                    .map(x -> FactionClaim.fromClaim(faction, x)));
        });
    }

    /**
     * This value is only temporary and is not getting updated once the object got created
     *
     * @return The total claims this faction has
     */
    public long getTotal() {
        return ClaimHandler
                .registryClaims(faction.getRegistry())
                .count();
    }

    public void claim(@NotNull Chunk<?> chunk) throws FactionNotInStorage, ChunkAlreadyClaimedException {
        claim(chunk.getWorld().getWorldName(), chunk.getX(), chunk.getZ());
    }

    public void claim(@NotNull String worldName, int x, int z)
            throws FactionNotInStorage, ChunkAlreadyClaimedException {
        World<?> world = ImprovedFactions.api().getWorld(worldName);
        if (world == null) return;

        Chunk<?> chunk = world.getChunkAt(x, z);
        try {
            ClaimHandler.protectChunk(faction.getRegistry(), chunk);
        } catch (ChunkAlreadyClaimedException e) {
            overclaim(chunk, faction, e.getRegistry());
        }
    }

    public FactionClaim<F> getClaim(@NotNull Chunk<?> chunk) throws FactionDoesntOwnChunkException {
        return FactionClaim.fromClaim(faction, chunk);
    }

    private void overclaim(@NotNull Chunk<?> chunk, @NotNull Faction<?> faction, @NotNull String claim)
            throws ChunkAlreadyClaimedException, FactionNotInStorage {
        if (faction.getRegistry().equals(claim)) throw new ChunkAlreadyClaimedException(claim);
        if (ClaimHandler.isManageableZone(claim)) throw new ChunkAlreadyClaimedException(claim);
        if (checkPower(faction, FactionHandler.getFaction(claim))) return;

        ClaimHandler.removeProtection(chunk);
    }

    private boolean checkPower(@NotNull Faction<?> sender, @NotNull Faction<?> target) {
        return sender.getActivePower().compareTo(target.getActiveMaxPower()) > 0;
    }

    public void unclaimAll() {
        if (claims == null) forceCalculate();

        claims.forEach((worldName, chunks) -> chunks.forEach(FactionClaim::unclaim));
        claims.clear();
    }

    public LinkedHashMap<String, Stream<FactionClaim<F>>> claims() {
        if (claims == null) forceCalculate();
        return claims;
    }

    public F faction() {
        return faction;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (FactionClaims<?>) obj;
        return Objects.equals(this.claims, that.claims) &&
                Objects.equals(this.faction, that.faction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(claims, faction);
    }

    @Override
    public String toString() {
        return "FactionClaims[" +
                "claims=" + claims + ", " +
                "faction=" + faction + ']';
    }


    public record FactionClaim<F extends Faction<F>>(F faction, String world, int x, int z) {

        public static <F extends Faction<F>> FactionClaim<F> fromClaim(@NotNull F faction,
                                                                       @NotNull Claim claim) {
            return new FactionClaim<>(faction, claim.getWorld(), claim.getX(), claim.getZ());
        }

        public static <F extends Faction<F>> FactionClaim<F> fromClaim(@NotNull F faction,
                                                                       @NotNull Chunk<?> chunk) throws FactionDoesntOwnChunkException {
            World<?> world = chunk.getWorld();
            int x = chunk.getX();
            int z = chunk.getZ();

            String registry = ClaimHandler.getWorldClaim(world).getRegistry(x, z);
            if (registry == null || !faction.getRegistry().equals(registry))
                throw new FactionDoesntOwnChunkException(registry);
            return new FactionClaim<>(faction, world.getWorldName(), x, z);
        }

        public void unclaim() {
            World<?> world = ImprovedFactions.api().getWorld(world());
            if (world == null) return;

            ClaimHandler.removeProtection(world.getChunkAt(x, z));
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
