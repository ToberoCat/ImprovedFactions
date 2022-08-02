package io.github.toberocat.core.utility.claim;

import org.bukkit.Chunk;

import static io.github.toberocat.core.utility.claim.ClaimManager.getChunkRegistry;

public class OverclaimManager {
    private static boolean isCorner(Chunk chunk) {
        Chunk[] neighbours = getNeighbourChunks(chunk);
        for (Chunk neighbour : neighbours) {
            String registry = getChunkRegistry(neighbour);
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
}
