package io.github.toberocat.core.utility.history.territory;

import org.bukkit.Chunk;

public class Territory {
    private String registry;
    private Chunk chunk;

    public Territory() {
    }

    public Territory(String registry, Chunk chunk) {
        this.registry = registry;
        this.chunk = chunk;
    }

    public String getRegistry() {
        return registry;
    }

    public void setRegistry(String registry) {
        this.registry = registry;
    }

    public Chunk getChunk() {
        return chunk;
    }

    public void setChunk(Chunk chunk) {
        this.chunk = chunk;
    }
}
