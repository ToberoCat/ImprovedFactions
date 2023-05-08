package io.github.toberocat.improvedFactions.core.utils.command.exceptions;

import org.jetbrains.annotations.NotNull;


public class CommandHasParentException extends RuntimeException {

    public CommandHasParentException(@NotNull String command) {
        super("Command " + command + " already has a parent. Can't have more than one parent");
    }
}
