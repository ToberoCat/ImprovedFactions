package io.github.toberocat.improvedFactions.claims;

import io.github.toberocat.improvedFactions.claims.component.Claim;
import io.github.toberocat.improvedFactions.claims.component.WorldClaim;
import io.github.toberocat.improvedFactions.event.EventExecutor;
import io.github.toberocat.improvedFactions.event.EventListener;
import io.github.toberocat.improvedFactions.exceptions.NoImplementationProvidedException;
import io.github.toberocat.improvedFactions.exceptions.chunk.ChunkAlreadyClaimedException;
import io.github.toberocat.improvedFactions.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.persistent.PersistentDataContainer;
import io.github.toberocat.improvedFactions.registry.ImplementationHolder;
import io.github.toberocat.improvedFactions.world.Chunk;
import io.github.toberocat.improvedFactions.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public abstract class ClaimHandler {

    public static final String SAFEZONE_REGISTRY = "__glb:safezone__";
    public static final String WARZONE_REGISTRY = "__glb:warzone__";
    public static final String UNCLAIMABLE_REGISTRY = "__glb:unclaimable__";

    private final Map<String, WorldClaim> claims;


    public ClaimHandler() {
        this.claims = new HashMap<>();
    }

    public static @NotNull ClaimHandler api() {
        ClaimHandler implementation = ImplementationHolder.claimHandler;
        if (implementation == null) throw new NoImplementationProvidedException("claim handler");
        return implementation;
    }

    protected abstract @NotNull WorldClaim createClaim(@NotNull World world);

    public void cacheAllWorlds() {
        ImprovedFactions.api()
                .getAllWorlds()
                .forEach(world -> claims.put(world.getWorldName(), createClaim(world)));
    }

    public void dispose() {
        claims.values().forEach(WorldClaim::disable);
        claims.clear();
    }

    public static int getRegistryColor(@NotNull String registry) {
        return switch (registry) {
            case SAFEZONE_REGISTRY -> 0x00bfff;
            case WARZONE_REGISTRY -> 0xb30000;
            case UNCLAIMABLE_REGISTRY -> 0x88858e;
            default -> throw new IllegalArgumentException("Registry has no zone color");
        };
    }

    public static @NotNull List<String> getZones() {
        return new ArrayList<>(List.of(SAFEZONE_REGISTRY, WARZONE_REGISTRY, UNCLAIMABLE_REGISTRY));
    }

    public static @Nullable String getZoneDisplay(@NotNull String registry) {
        return switch (registry) {
            case SAFEZONE_REGISTRY -> "territory.safezone";
            case WARZONE_REGISTRY -> "territory.warzone";
            default -> null;
        };
    }

    public static boolean isManageableZone(@Nullable String registry) {
        return getZones().contains(registry);
    }

    public @NotNull Stream<Claim> registryClaims(@NotNull String registry) {
        return claims.values()
                .stream()
                .flatMap(WorldClaim::getAllClaims)
                .filter(x -> x.registry().equals(registry));
    }

    public void protectChunk(@NotNull String registry, @NotNull Chunk chunk) throws ChunkAlreadyClaimedException {
        PersistentDataContainer container = chunk.getDataContainer();
        String claimed = container.getString(PersistentDataContainer.CLAIM_KEY);

        if (claimed != null) throw new ChunkAlreadyClaimedException(claimed);

        container.set(PersistentDataContainer.CLAIM_KEY, registry);

        WorldClaim worldClaim = getWorldClaim(chunk.getWorld());
        worldClaim.add(registry, chunk.getX(), chunk.getZ());

        EventExecutor.getExecutor().protectChunk(chunk, registry);
    }

    public void forEach(BiConsumer<String, WorldClaim> consumer) {
        claims.forEach(consumer);
    }

    public @NotNull WorldClaim getWorldClaim(@NotNull World world) {
        WorldClaim claim = claims.get(world.getWorldName());
        if (claim == null) {
            claim = createClaim(world);
            claims.put(world.getWorldName(), claim);
        }

        return claim;
    }
}
