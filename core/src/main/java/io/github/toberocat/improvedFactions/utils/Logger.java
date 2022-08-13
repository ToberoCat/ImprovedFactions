package io.github.toberocat.improvedFactions.utils;

import io.github.toberocat.improvedFactions.exceptions.NoImplementationProvidedException;
import io.github.toberocat.improvedFactions.registry.ImplementationHolder;
import org.jetbrains.annotations.NotNull;

public interface Logger {

    static @NotNull Logger api() {
        Logger implementation = ImplementationHolder.logger;
        if (implementation == null) throw new NoImplementationProvidedException("logger");
        return implementation;
    }

    void logInfo(@NotNull String message);

    void logWarning(@NotNull String message);

    void logError(@NotNull String message);

    void logException(@NotNull Exception e);
}
