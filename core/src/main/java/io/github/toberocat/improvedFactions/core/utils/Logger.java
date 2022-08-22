package io.github.toberocat.improvedFactions.core.utils;

import io.github.toberocat.improvedFactions.core.exceptions.NoImplementationProvidedException;
import io.github.toberocat.improvedFactions.core.registry.ImplementationHolder;
import org.jetbrains.annotations.NotNull;

public interface Logger {

    static @NotNull Logger api() {
        Logger implementation = ImplementationHolder.logger;
        if (implementation == null) throw new NoImplementationProvidedException("logger");
        return implementation;
    }

    void logInfo(@NotNull String message, Object... placeholders);

    void logWarning(@NotNull String message);

    void logError(@NotNull String message);

    void logException(@NotNull Exception e);
}
