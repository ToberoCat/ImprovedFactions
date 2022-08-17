package io.github.toberocat.improvedFactions.core.exceptions.faction;

import org.jetbrains.annotations.NotNull;

public class FactionNotInStorage extends Exception {

    public enum StorageType {
        LOCAL_FILE,
        DATABASE
    }

    private final String registry;

    public FactionNotInStorage(@NotNull String registry, @NotNull StorageType type) {
        super(String.format("Couldn't find requested faction %s in storage type %s", registry, type));
        this.registry = registry;
    }

    public String getRegistry() {
        return registry;
    }
}
