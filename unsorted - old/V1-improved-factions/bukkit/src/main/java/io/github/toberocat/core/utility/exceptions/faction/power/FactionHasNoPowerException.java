package io.github.toberocat.core.utility.exceptions.faction.power;

import org.jetbrains.annotations.NotNull;

public class FactionHasNoPowerException extends Exception {
    public FactionHasNoPowerException(@NotNull String registry) {
        super("The faction " + registry + " has no power left anymore");
    }
}
