package io.github.toberocat.core.utility.exceptions.faction;

import org.jetbrains.annotations.NotNull;

public class FactionNotFoundException extends Exception {
    public FactionNotFoundException(@NotNull String registry) {
        super(String.format("Couldn't find requested faction %s", registry));
    }
}
