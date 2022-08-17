package io.github.toberocat.improvedFactions.core.exceptions.faction;

import org.jetbrains.annotations.NotNull;

public class FactionHandlerNotFound extends RuntimeException {
    public FactionHandlerNotFound() {
        super("FactionHandler wasn't to be found. This is a critical bug, please report it to the dev on discord or github");
    }

    public FactionHandlerNotFound(@NotNull String msg) {
        super(msg);
    }
}
