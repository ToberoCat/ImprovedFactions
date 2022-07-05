package io.github.toberocat.improvedfactions.data;

import io.github.toberocat.improvedfactions.mesh.HeightCache;
import org.bukkit.util.Vector;

public record PositionPair(int x, int z, HeightCache cache) {
    public Vector toVector(double y) {
        return new Vector(x, y, z);
    }

    @Override
    public String toString() {
        return "ChunkKey{" +
                "x=" + x +
                ", z=" + z +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PositionPair pair)) return false;
        return x == pair.x && z == pair.z;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + z;
        return result;
    }
}
