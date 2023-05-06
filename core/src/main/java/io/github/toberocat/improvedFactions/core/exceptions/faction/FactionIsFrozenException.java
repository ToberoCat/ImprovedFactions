package io.github.toberocat.improvedFactions.core.exceptions.faction;

import io.github.toberocat.improvedFactions.core.exceptions.TranslatableException;
import io.github.toberocat.improvedFactions.core.translator.PlaceholderBuilder;
import org.jetbrains.annotations.NotNull;

public class FactionIsFrozenException extends TranslatableException {

    private final String registry;

    public FactionIsFrozenException(@NotNull String registry) {
        super("exceptions.faction-is-frozen", new PlaceholderBuilder()
                .placeholder("registry", registry)
                .getPlaceholders());
        this.registry = registry;
    }

    public String getRegistry() {
        return registry;
    }
}
