package io.github.toberocat.improvedfactions.mesh;

import io.github.toberocat.improvedfactions.exceptions.GivenWorldDoesntExist;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;

public record HeightCache(LinkedHashSet<Integer> heights) {

    public static HeightCache fromXZ(@NotNull String worldName, int x, int z) {
        World world = Bukkit.getWorld(worldName);
        if (world == null) throw new GivenWorldDoesntExist("The world " + worldName + " wasn't to be found");
        HeightCache cache = new HeightCache(new LinkedHashSet<>());
        cache.scanHeights(world, x, z);

        return cache;
    }

    public void scanHeights(@NotNull World world, int x, int z) {
        Material lastMaterial = Material.BEDROCK;
        heights.clear();
        for (int y = world.getMinHeight(); y < world.getMaxHeight(); y++) {
            Block block = world.getBlockAt(x, y, z);
            Material now = block.getType();

            if (!now.isSolid() && lastMaterial.isSolid()) heights.add(y);
            lastMaterial = now;
        }
    }
}
