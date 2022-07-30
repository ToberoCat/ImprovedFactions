package io.github.toberocat.core.utility.exceptions.faction;

import org.jetbrains.annotations.NotNull;

public class FactionIsFrozenException extends Exception {
    public FactionIsFrozenException(@NotNull String registry) {
        super("You can't make any actions on faction " + registry + ", due to it being frozen");
    }
}
