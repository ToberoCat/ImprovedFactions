package io.github.toberocat.improvedFactions.core.utils.command.exceptions;

import io.github.toberocat.improvedFactions.core.exceptions.TranslatableException;
import org.jetbrains.annotations.NotNull;


public class CommandException extends TranslatableException {
    public CommandException(@NotNull String translationKey) {
        super(translationKey);
    }
}
