package io.github.toberocat.improvedFactions.core.exceptions.faction;

import io.github.toberocat.improvedFactions.core.exceptions.TranslatableException;
import io.github.toberocat.improvedFactions.core.translator.PlaceholderBuilder;
import org.jetbrains.annotations.NotNull;

public class FactionNotFoundException extends TranslatableException {
    public FactionNotFoundException(@NotNull String registry) {
        super("exceptions.faction-not-found", new PlaceholderBuilder()
                .placeholder("registry", registry)
                .getPlaceholders());
    }
}
