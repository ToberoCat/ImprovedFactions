package io.github.toberocat.improvedfactions.spigot.world;

import io.github.toberocat.improvedFactions.core.world.Chunk;
import io.github.toberocat.improvedFactions.core.world.World;
import org.jetbrains.annotations.NotNull;

public class SpigotWorld implements World<org.bukkit.World> {

    private final org.bukkit.World world;

    public SpigotWorld(org.bukkit.World world) {
        this.world = world;
    }

    @Override
    public @NotNull String getWorldName() {
        return world.getName();
    }

    @Override
    public @NotNull Chunk getChunkAt(int x, int z) {
        return new SpigotChunk(this, world.getChunkAt(x, z));
    }

    @NotNull
    @Override
    public org.bukkit.World getRaw() {
        return world;
    }
}
