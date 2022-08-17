package io.github.toberocat.improvedFactions.core.exceptions.chunk;

import org.jetbrains.annotations.NotNull;

public class ChunkAlreadyClaimedException extends Exception {

    private final String registry;

    public ChunkAlreadyClaimedException(@NotNull String registry) {
        super("The chunk got already claimed");
        this.registry = registry;
    }

    public String getRegistry() {
        return registry;
    }
}
