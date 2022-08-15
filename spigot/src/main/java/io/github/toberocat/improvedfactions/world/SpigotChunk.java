package io.github.toberocat.improvedfactions.world;

import io.github.toberocat.improvedFactions.persistent.PersistentDataContainer;
import io.github.toberocat.improvedFactions.world.Chunk;
import io.github.toberocat.improvedFactions.world.World;
import io.github.toberocat.improvedfactions.MainIF;
import io.github.toberocat.improvedfactions.persistent.SpigotPersistentData;
import org.jetbrains.annotations.NotNull;

public class SpigotChunk implements Chunk {

    private final World world;
    private final org.bukkit.Chunk chunk;
    private final MainIF plugin;

    public SpigotChunk(World world, org.bukkit.Chunk chunk, MainIF plugin) {
        this.world = world;
        this.chunk = chunk;
        this.plugin = plugin;
    }

    @Override
    public @NotNull World getWorld() {
        return world;
    }

    @Override
    public @NotNull PersistentDataContainer getDataContainer() {
        return new SpigotPersistentData(plugin, chunk.getPersistentDataContainer());
    }

    @Override
    public int getX() {
        return chunk.getX();
    }

    @Override
    public int getZ() {
        return chunk.getZ();
    }
}
