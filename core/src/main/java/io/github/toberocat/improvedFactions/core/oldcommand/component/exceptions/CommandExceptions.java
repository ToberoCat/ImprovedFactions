package io.github.toberocat.improvedFactions.core.oldcommand.component.exceptions;

import org.jetbrains.annotations.NotNull;


public class CommandExceptions extends Exception {
    private final String message;

    public CommandExceptions(@NotNull String message) {
        this.message = message;
    }

    public String getMessageId() {
        return message;
    }
}
