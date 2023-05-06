package io.github.toberocat.improvedFactions.core.exceptions.faction;

import io.github.toberocat.improvedFactions.core.exceptions.TranslatableException;
import io.github.toberocat.improvedFactions.core.translator.PlaceholderBuilder;
import org.jetbrains.annotations.NotNull;

public class FactionNotInStorage extends TranslatableException {

    public enum StorageType {
        LOCAL_FILE,
        DATABASE
    }

    private final String registry;

    public FactionNotInStorage(@NotNull String registry, @NotNull StorageType type) {
        super("exceptions.faction-not-stored", new PlaceholderBuilder()
                .placeholder("registry", registry)
                .placeholder("type", type.name())
                .getPlaceholders());
        this.registry = registry;
    }

    public String getRegistry() {
        return registry;
    }
}
