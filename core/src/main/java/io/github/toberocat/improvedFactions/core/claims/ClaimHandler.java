package io.github.toberocat.improvedFactions.core.claims;

import io.github.toberocat.improvedFactions.core.claims.component.Claim;
import io.github.toberocat.improvedFactions.core.claims.worldclaim.WorldClaim;
import io.github.toberocat.improvedFactions.core.claims.worldclaim.handler.WorldClaimHandler;
import io.github.toberocat.improvedFactions.core.event.EventExecutor;
import io.github.toberocat.improvedFactions.core.exceptions.chunk.ChunkAlreadyClaimedException;
import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.persistent.PersistentHandler;
import io.github.toberocat.improvedFactions.core.persistent.component.PersistentWrapper;
import io.github.toberocat.improvedFactions.core.world.Chunk;
import io.github.toberocat.improvedFactions.core.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class ClaimHandler {

    public static final String SAFEZONE_REGISTRY = "__glb:safezone__";
    public static final String WARZONE_REGISTRY = "__glb:warzone__";
    public static final String UNCLAIMABLE_REGISTRY = "__glb:unclaimable__";

    private static final Map<String, WorldClaim> claims = new HashMap<>();

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


    public static void cacheAllWorlds() {
        ImprovedFactions.api()
                .getAllWorlds()
                .forEach(world -> claims.put(world.getWorldName(), WorldClaimHandler.api()
                        .createWorldClaim(world)));
    }

    public static void dispose() {
        claims.values().forEach(WorldClaim::dispose);
        claims.clear();
    }

    public static @NotNull Stream<Claim> registryClaims(@NotNull String registry) {
        return claims.values()
                .stream()
                .flatMap(WorldClaim::getAllClaims)
                .filter(x -> x.getRegistry().equals(registry));
    }

    public static void protectChunk(@NotNull String registry, @NotNull Chunk<?> chunk) throws ChunkAlreadyClaimedException {
        PersistentWrapper container = chunk.getDataContainer();
        String claimed = container.get(PersistentHandler.CLAIM_KEY);

        if (claimed != null) throw new ChunkAlreadyClaimedException(claimed);

        container.set(PersistentHandler.CLAIM_KEY, registry);

        WorldClaim worldClaim = getWorldClaim(chunk.getWorld());
        worldClaim.add(registry, chunk.getX(), chunk.getZ());

        EventExecutor.getExecutor().protectChunk(chunk, registry);
    }

    public static void removeProtection(@NotNull Chunk<?> chunk) {
        PersistentWrapper container = chunk.getDataContainer();

        String previousRegistry = container.get(PersistentHandler.CLAIM_KEY);
        container.remove(PersistentHandler.CLAIM_KEY);

        getWorldClaim(chunk.getWorld()).remove(chunk.getX(), chunk.getZ());
        EventExecutor.getExecutor().removeProtection(chunk, previousRegistry);
    }

    public static void forEach(BiConsumer<String, WorldClaim> consumer) {
        claims.forEach(consumer);
    }

    public static @NotNull WorldClaim getWorldClaim(@NotNull World<?> world) {
        return claims
                .computeIfAbsent(world.getWorldName(), k ->
                        WorldClaimHandler.api().createWorldClaim(world));
    }
}
