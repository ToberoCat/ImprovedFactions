package io.github.toberocat.improvedfactions.exceptions;

import org.jetbrains.annotations.NotNull;

public class GivenWorldDoesntExist extends RuntimeException {
    public GivenWorldDoesntExist(@NotNull String message) {
        super(message);
    }
}
