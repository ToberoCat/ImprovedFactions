package io.github.toberocat.improvedFactions.core.claims.worldclaim.local;

import java.util.Objects;

public record XZPair(int x, int z) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        XZPair xzPair = (XZPair) o;
        return x == xzPair.x && z == xzPair.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, z);
    }
}
