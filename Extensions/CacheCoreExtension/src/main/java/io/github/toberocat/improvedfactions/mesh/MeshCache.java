package io.github.toberocat.improvedfactions.mesh;

import io.github.toberocat.core.factions.FactionUtility;
import io.github.toberocat.core.utility.async.AsyncTask;
import io.github.toberocat.core.utility.claim.ClaimManager;
import io.github.toberocat.improvedfactions.events.WorldCacheCreatedEvent;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class MeshCache {

    private final HashMap<String, WorldCache> worldCache;

    public MeshCache() {
        this.worldCache = new LinkedHashMap<>();

        for (World world : Bukkit.getWorlds()) worldCache.put(world.getName(), new WorldCache(world.getName()));
    }

    public void dispose() {
        worldCache.values().forEach(WorldCache::dispose);
        worldCache.clear();
    }

    /* Zones / Factions */
    public void cacheFactions() {
        FactionUtility.getAllFactionsStream().forEach(this::cacheRegistry);
    }

    public void cacheZones(boolean cacheUnclaimable) {
        ClaimManager.getZones(cacheUnclaimable).forEach(this::cacheRegistry);
    }

    public void removeCache(@NotNull String registry) {
        worldCache.values().forEach(cache -> cache.removeCache(registry));
    }

    public void cacheRegistry(@NotNull String registry) {
        worldCache.values().forEach((cache) -> cache.cacheRegistry(registry));
    }

    /* Worlds */

    public AsyncTask<WorldCache> createWorldCache(@NotNull final World world, final boolean cacheUnclaimable) {
        final String worldName = world.getName();

        WorldCache cache = new WorldCache(worldName);
        worldCache.put(worldName, cache); // Mark as registered as soon as possible

        // Cache all chunks async
        return AsyncTask.run(() -> {
            FactionUtility.getAllFactionsStream().forEach(cache::cacheRegistry);
            ClaimManager.getZones(cacheUnclaimable).forEach(cache::cacheRegistry);

            // Call event when everything setup
            AsyncTask.runSync(() -> Bukkit.getPluginManager().callEvent(new WorldCacheCreatedEvent(cache)));
        });
    }

    public void unloadWorldCache(@NotNull World world) {
        worldCache.remove(world.getName());
    }

    public @NotNull AsyncTask<WorldCache> requestWorldCache(@NotNull final String worldName, final boolean cacheUnclaimable) {
        if (worldCache.containsKey(worldName)) return AsyncTask.returnItem(worldCache.get(worldName));

        World world = Bukkit.getWorld(worldName);
        if (world == null) return AsyncTask.returnItem(null);

        return createWorldCache(world, cacheUnclaimable);
    }

    /* Chunk */

    public void cacheChunk(@NotNull String registry, @NotNull Chunk chunk) {
        WorldCache cache = worldCache.get(chunk.getWorld().getName());
        cache.cacheChunk(registry, chunk);
        cache.updateChunk(registry, chunk);
    }

    public void unloadCacheChunk(@NotNull String registry, @NotNull Chunk chunk) {
        WorldCache cache = worldCache.get(chunk.getWorld().getName());
        cache.removeChunkCache(registry, chunk);
        cache.updateChunk(registry, chunk);
    }

    /* Blocks */
    public void updateBlockCache(@NotNull String registry, @NotNull Block block) {
        WorldCache cache = worldCache.get(block.getWorld().getName());
        cache.updateBlock(registry, block);
    }

    /* General  */

    public WorldCache getCache(@NotNull String world) {
        return worldCache.get(world);
    }
}
