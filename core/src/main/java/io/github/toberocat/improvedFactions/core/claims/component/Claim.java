package io.github.toberocat.improvedFactions.core.claims.component;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public final class Claim {
    private final int x;
    private final int z;
    private final String world;
    private final Function<Claim, String> compute;

    public Claim(@NotNull String world, int x, int z, @NotNull Function<Claim, String> registryComputer) {
        this.x = x;
        this.z = z;
        this.world = world;
        this.compute = registryComputer;
    }


    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public String getWorld() {
        return world;
    }

    public String getRegistry() {
        return compute.apply(this);
    }
}
