package io.github.toberocat.improvedfactions.mesh;

import io.github.toberocat.improvedfactions.data.ChunkKey;
import io.github.toberocat.improvedfactions.data.Line;
import io.github.toberocat.improvedfactions.data.RenderLocation;
import io.github.toberocat.improvedfactions.data.Shape;
import org.bukkit.World;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

public class ShapeCalculator {
    private final HashMap<UUID, ArrayList<ChunkKey>> groups;
    private final HashMap<ChunkKey, UUID> ids;

    public ShapeCalculator(HashMap<UUID, ArrayList<ChunkKey>> groups, HashMap<ChunkKey, UUID> ids) {
        this.groups = groups;
        this.ids = ids;
    }

    public Shape createGroupShape(@NotNull World world, @NotNull UUID id) {
        if (!groups.containsKey(id)) return null;
        ArrayList<ChunkKey> group = groups.get(id);
        String worldName = world.getName();

        int minX = Integer.MAX_VALUE;
        int minZ = Integer.MAX_VALUE;

        int maxX = Integer.MIN_VALUE;
        int maxZ = Integer.MIN_VALUE;
        for (ChunkKey key : group) {
            minX = Math.min(key.x(), minX);
            minZ = Math.min(key.z(), minZ);

            maxX = Math.max(key.x(), maxX);
            maxZ = Math.max(key.z(), maxZ);
        }

        ChunkKey min = new ChunkKey(minX, minZ);
        ChunkKey max = new ChunkKey(maxX, maxZ);

        // Create lines
        LinkedList<Line> lines = new LinkedList<>();
        for (int x = min.x(); x <= max.x(); x++)
            for (int z = min.z(); z <= max.z(); z++) {
                if (!group.contains(new ChunkKey(x, z))) continue;


                int x16 = x * 16;
                int z16 = z * 16;
                RenderLocation corner = new RenderLocation(x16 + 15, z16 + 15, worldName);

                // Top
                if (showLine(id, x , z - 1))
                    lines.add(new Line(
                        new RenderLocation(x16, z16, worldName),
                        new RenderLocation(x16 + 15, z16, worldName)
                ));

                // Bottom
                if (showLine(id, x , z + 1))
                    lines.add(new Line(
                        new RenderLocation(x16, z16 + 15, worldName),
                       corner
                ));

                // Right
                if (showLine(id, x + 1, z))
                    lines.add(new Line(
                        new RenderLocation(x16 + 15, z16, worldName),
                        corner
                ));

                // Left
                if (showLine(id, x - 1, z))
                lines.add(new Line(
                        new RenderLocation(x16, z16, worldName),
                        new RenderLocation(x16, z16 + 15, worldName)
                ));


            }

        return new Shape(lines, new BoundingBox(
                min.x(), world.getMinHeight(), min.z(),
                max.x(), world.getMaxHeight(), max.z()
        ));
    }

    private boolean showLine(UUID groupId, int checkX, int checkZ) {
        ChunkKey key = new ChunkKey(checkX, checkZ);
        if (!ids.containsKey(key)) return true;

        return ids.get(key) != groupId;
    }
}