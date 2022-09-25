package io.github.toberocat.improvedFactions.core.claims;

import io.github.toberocat.improvedFactions.core.claims.component.Claim;
import io.github.toberocat.improvedFactions.core.claims.worldclaim.WorldClaim;
import io.github.toberocat.improvedFactions.core.claims.worldclaim.handler.WorldClaimHandler;
import io.github.toberocat.improvedFactions.core.claims.zone.Zone;
import io.github.toberocat.improvedFactions.core.event.EventExecutor;
import io.github.toberocat.improvedFactions.core.exceptions.chunk.ChunkAlreadyClaimedException;
import io.github.toberocat.improvedFactions.core.handler.ConfigHandler;
import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.world.Chunk;
import io.github.toberocat.improvedFactions.core.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class ClaimHandler {

    public static final String WILDERNESS_REGISTRY = "__glb:wilderness__";

    private static final Map<String, WorldClaim> claims = new HashMap<>();
    private static final Map<String, Zone> zones = new HashMap<>();

    public static @Nullable Zone getZone(@NotNull String registry) {
        return zones.get(registry);
    }

    public static Set<String> getZones() {
        return zones.keySet();
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
        zones.clear();
    }

    public static void reload() {
        zones.clear();
        ConfigHandler api = ConfigHandler.api();
        api.getSubSections("zones").stream()
                .map(zone -> {
                    String root = "zones." + zone;
                    String translationId = api.getString(root + ".translation-id",
                            "territory." + zone);
                    String registry = api.getString(root + ".registry", "__glb:" + zone + "__");
                    int color = api.getInt(root + ".color", 0);
                    if (!api.getBool(root + ".managed", true))
                        return new Zone(zone, translationId, registry, color, false, false,
                                false);

                    boolean protection = api.getBool(root + ".protection", true);
                    boolean pvp = api.getBool(root + ".pvp", false);
                    return new Zone(zone, translationId, registry, color, true, protection, pvp);
                })
                .forEach(x -> zones.put(x.registry(), x));
    }

    public static @NotNull Stream<Claim> registryClaims(@NotNull String registry) {
        return claims.values()
                .stream()
                .flatMap(WorldClaim::getAllClaims)
                .filter(x -> x.getRegistry().equals(registry));
    }

    public static void protectChunk(@NotNull String registry, @NotNull Chunk<?> chunk)
            throws ChunkAlreadyClaimedException {
        WorldClaim worldClaim = getWorldClaim(chunk.getWorld());
        String claimed = worldClaim.getRegistry(chunk.getX(), chunk.getZ());

        if (claimed != null) throw new ChunkAlreadyClaimedException(claimed);

        worldClaim.add(registry, chunk.getX(), chunk.getZ());
        EventExecutor.getExecutor().protectChunk(chunk, registry);
    }

    public static void removeProtection(@NotNull Chunk<?> chunk) {
        WorldClaim worldClaim = getWorldClaim(chunk.getWorld());

        String previousRegistry = worldClaim.getRegistry(chunk.getX(), chunk.getZ());

        worldClaim.remove(chunk.getX(), chunk.getZ());
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
