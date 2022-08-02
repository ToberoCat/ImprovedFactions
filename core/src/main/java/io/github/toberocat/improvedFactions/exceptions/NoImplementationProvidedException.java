package io.github.toberocat.improvedFactions.exceptions;

import org.jetbrains.annotations.NotNull;

public class NoImplementationProvidedException extends RuntimeException {
    public NoImplementationProvidedException(@NotNull String impl) {
        super("The " + impl + " implementation hasn't been set yet");
    }
}
