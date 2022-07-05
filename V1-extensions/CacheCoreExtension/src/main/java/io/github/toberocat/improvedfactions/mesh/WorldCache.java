package io.github.toberocat.improvedfactions.mesh;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.async.AsyncTask;
import io.github.toberocat.improvedfactions.data.ChunkKey;
import io.github.toberocat.improvedfactions.data.Line;
import io.github.toberocat.improvedfactions.data.PositionPair;
import io.github.toberocat.improvedfactions.data.Shape;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Stream;

public class WorldCache {

    private final HashMap<String, HashMap<UUID, Shape>> shapes;
    private final HashMap<String, ArrayList<ChunkKey>> chunkScans;
    private final HashMap<ChunkKey, UUID> ids;
    private final String worldName;
    HashMap<UUID, ArrayList<ChunkKey>> groups;

    public WorldCache(@NotNull String worldName) {
        this.worldName = worldName;
        this.shapes = new HashMap<>();

        this.chunkScans = new HashMap<>();
        this.ids = new HashMap<>();
        this.groups = new HashMap<>();
    }

    public void dispose() {
        shapes.clear();
        chunkScans.clear();
        ids.clear();
        groups.clear();
    }

    public void removeCache(@NotNull String registry) {
        shapes.remove(registry);
        chunkScans.remove(registry);
    }

    public void cacheRegistry(@NotNull String registry) {
        if (shapes.containsKey(registry)) return;
        recalculate(registry);
    }

    public Stream<Map.Entry<String, Stream<Shape>>> getShapes(BoundingBox player) {
        List<Map.Entry<String, HashMap<UUID, Shape>>> copy = new ArrayList<>(shapes.entrySet());
        return copy.stream().map(entry -> {
            Stream<Shape> items = entry.getValue().values().stream().filter(x -> player.overlaps(x.box()));

            return Map.entry(entry.getKey(), items);
        });
    }

    /**
     * Scans all claimed chunks and adds them to claim registry list
     *
     * @param registry The registry of a faction that should get used for scanning
     */
    public void scanChunks(@NotNull String registry) {
        if (!chunkScans.containsKey(registry)) chunkScans.put(registry, new ArrayList<>());

        ArrayList<ChunkKey> scans = chunkScans.get(registry);
        MainIF.getIF().getClaimManager()
                .registryClaims(registry, worldName)
                .forEach((claim -> scans.add(new ChunkKey(claim.getX(), claim.getY()))));
    }

    /**
     * Add a chunk to the registry as claimed
     */
    public void cacheChunk(@NotNull String registry, @NotNull Chunk chunk) {
        if (!shapes.containsKey(registry)) chunkScans.put(registry, new ArrayList<>());

        ChunkKey key = new ChunkKey(chunk.getX(), chunk.getZ());
        if (chunkScans.get(registry).contains(key)) return;
        chunkScans.get(registry).add(key);
    }

    public void removeChunkCache(@NotNull String registry, @NotNull Chunk chunk) {
        if (!shapes.containsKey(registry)) chunkScans.put(registry, new ArrayList<>());
        chunkScans.get(registry).remove(new ChunkKey(chunk.getX(), chunk.getZ()));
    }

    public void updateBlock(@NotNull String registry, @NotNull Block block) {
        if (!shapes.containsKey(registry)) return;

        int blockX = block.getX();
        int blockZ = block.getZ();

        if (blockX != 0 && blockX != 15 && blockZ != 0 && blockZ != 15) return; // Is block a line block?

        World world = Bukkit.getWorld(worldName);
        if (world == null) return;

        if (!chunkScans.containsKey(registry)) scanChunks(registry);
        Chunk action = block.getChunk();
        ChunkKey key = new ChunkKey(action.getX(), action.getZ());
        ArrayList<ChunkKey> scans = chunkScans.get(registry);
        if (!scans.contains(key)) return;

        if (!ids.containsKey(key)) { // The chunk hasn't had a shape anyways, so calculate it
            cacheChunk(registry, action);
            updateChunk(registry, action);
            return;
        }

        UUID id = ids.get(key);

        if (!groups.containsKey(id)) groups.put(id, new ArrayList<>());
        groups.get(id).add(key);

        if (!shapes.get(registry).containsKey(id)) { // Isn't there a cached shape anyways,
            // just calculate it. Will have the updated block
            cacheChunk(registry, action);
            updateChunk(registry, action);
            return;
        }
        Shape shape = shapes.get(registry).get(id);
        PositionPair copy = new PositionPair(blockX, blockZ, null);
        for (Line line : shape.lines()) {
            int index = line.getLocations().indexOf(copy);
            if (index == -1) continue;

            PositionPair pair = line.getLocations().get(index);
            if (pair == null) continue;

            pair.cache().scanHeights(world, blockX, blockZ);
            return;
        }
    }

    public void updateChunk(@NotNull String registry, @NotNull Chunk action) {
        World world = Bukkit.getWorld(worldName);
        if (world == null) return;

        if (!chunkScans.containsKey(registry)) scanChunks(registry);
        ChunkKey key = new ChunkKey(action.getX(), action.getZ());

        // Clear previous shape cache
        shapes.remove(registry);

        ArrayList<ChunkKey> scans = chunkScans.get(registry);
        if (!scans.contains(key)) {
            UUID id = getUuid(action.getX(), action.getZ());
            ids.remove(key, id);

            if (!groups.containsKey(id)) groups.put(id, new ArrayList<>());
            groups.get(id).remove(key);

            ShapeCalculator calculator = new ShapeCalculator(groups, ids);
            Shape shape = calculator.createGroupShape(world, id);
            if (shape == null) {
                shapes.remove(registry);
                return;
            }

            if (!shapes.containsKey(registry)) shapes.put(registry, new HashMap<>());
            shapes.get(registry).put(id, shape);
        } else {
            UUID id = getUuid(action.getX(), action.getZ());
            ids.put(key, id);

            if (!groups.containsKey(id)) groups.put(id, new ArrayList<>());
            groups.get(id).add(key);

            ShapeCalculator calculator = new ShapeCalculator(groups, ids);
            Shape shape = calculator.createGroupShape(world, id);
            if (shape == null) return;

            if (!shapes.containsKey(registry)) shapes.put(registry, new HashMap<>());
            shapes.get(registry).put(id, shape);
        }
    }

    private UUID getUuid(int x, int z) {
        ChunkKey neighbourWithId = AsyncTask.find(neighbours(x, z), ids::containsKey);
        return neighbourWithId == null ? UUID.randomUUID() : ids.get(neighbourWithId);
    }

    public void recalculate(@NotNull String registry) {
        World world = Bukkit.getWorld(worldName);
        if (world == null) return;

        if (!chunkScans.containsKey(registry)) scanChunks(registry);

        // Clear previous shape cache
        shapes.remove(registry);
        ids.clear();
        groups.clear();

        ArrayList<ChunkKey> scans = chunkScans.get(registry);

        // Group chunks into one shape per continuous border
        for (ChunkKey chunk : scans) {
            // Get the shape id the chunk belongs to
            UUID id = getUuid(chunk.x(), chunk.z());
            ids.put(chunk, id);

            if (!groups.containsKey(id)) groups.put(id, new ArrayList<>());
            groups.get(id).add(chunk);
        }

        ShapeCalculator calculator = new ShapeCalculator(groups, ids);

        // Create lines from groups
        for (UUID id : groups.keySet()) {
            Shape shape = calculator.createGroupShape(world, id);
            if (shape == null) continue;

            if (!shapes.containsKey(registry)) shapes.put(registry, new HashMap<>());
            shapes.get(registry).put(id, shape);
        }
    }

    private List<ChunkKey> neighbours(int x, int z) {
        return List.of(
                new ChunkKey(x - 1, z),
                new ChunkKey(x + 1, z),
                new ChunkKey(x, z - 1),
                new ChunkKey(x, z + 1)
        );
    }
}
