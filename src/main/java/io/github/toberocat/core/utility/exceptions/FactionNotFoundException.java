package io.github.toberocat.core.utility.exceptions;

import org.jetbrains.annotations.NotNull;

public class FactionNotFoundException extends RuntimeException {
    public FactionNotFoundException(@NotNull String registry) {
        super(String.format("Couldn't find requested faction %s", registry));
    }
}
