package io.github.toberocat.improvedfactions.world;

import io.github.toberocat.improvedFactions.world.Chunk;
import io.github.toberocat.improvedFactions.world.World;
import io.github.toberocat.improvedfactions.MainIF;
import org.jetbrains.annotations.NotNull;

public class SpigotWorld implements World {

    private final org.bukkit.World world;
    private final MainIF plugin;

    public SpigotWorld(org.bukkit.World world, MainIF plugin) {
        this.world = world;
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getWorldName() {
        return world.getName();
    }

    @Override
    public @NotNull Chunk getChunkAt(int x, int z) {
        return new SpigotChunk(this, world.getChunkAt(x, z), plugin);
    }
}
