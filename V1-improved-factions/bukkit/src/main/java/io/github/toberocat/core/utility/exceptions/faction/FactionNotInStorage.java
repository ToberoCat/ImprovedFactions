package io.github.toberocat.core.utility.exceptions.faction;

import org.jetbrains.annotations.NotNull;

public class FactionNotInStorage extends Exception {

    public enum StorageType {
        LOCAL_FILE,
        DATABASE
    }

    public FactionNotInStorage(@NotNull String registry, @NotNull StorageType type) {
        super(String.format("Couldn't find requested faction %s in storage type %s", registry, type));
    }
}
