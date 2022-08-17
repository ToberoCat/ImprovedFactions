package io.github.toberocat.improvedfactions.world;

import com.github.f4b6a3.uuid.UuidCreator;
import io.github.toberocat.improvedFactions.core.persistent.component.PersistentWrapper;
import io.github.toberocat.improvedFactions.core.utils.CUtils;
import io.github.toberocat.improvedFactions.core.world.Chunk;
import io.github.toberocat.improvedFactions.core.world.World;
import org.jetbrains.annotations.NotNull;

public class SpigotChunk implements Chunk<org.bukkit.Chunk> {

    private final World<?> world;
    private final org.bukkit.Chunk chunk;
    private final PersistentWrapper wrapper;

    public SpigotChunk(World<?> world, org.bukkit.Chunk chunk) {
        this.world = world;
        this.chunk = chunk;
        this.wrapper = new PersistentWrapper(UuidCreator.getNameBasedMd5(CUtils
                .convertToByteArray(chunk.getX(), chunk.getZ(), world.hashCode())));
    }

    @Override
    public @NotNull World<?> getWorld() {
        return world;
    }

    @Override
    public @NotNull PersistentWrapper getDataContainer() {
        return wrapper;
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
