package io.github.toberocat.improvedfactions.spigot.world;

import io.github.toberocat.improvedFactions.core.world.Chunk;
import io.github.toberocat.improvedFactions.core.world.World;
import org.jetbrains.annotations.NotNull;

public class SpigotChunk implements Chunk {

    private final World world;
    private final org.bukkit.Chunk chunk;

    public SpigotChunk(World world, org.bukkit.Chunk chunk) {
        this.world = world;
        this.chunk = chunk;
    }

    @Override
    public @NotNull World getWorld() {
        return world;
    }

    @Override
    public @NotNull String getClaimRegistry() {
        return null; // ToDo implement receiving claim registry
    }

    @Override
    public int getX() {
        return chunk.getX();
    }

    @Override
    public int getZ() {
        return chunk.getZ();
    }

    @NotNull
    @Override
    public org.bukkit.Chunk getRaw() {
        return chunk;
    }
}
