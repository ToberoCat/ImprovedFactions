package io.github.toberocat.improvedfactions.data;

public record ChunkKey(int x, int z) {

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
        if (!(o instanceof ChunkKey key)) return false;
        return x == key.x && z == key.z;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + z;
        return result;
    }
}
