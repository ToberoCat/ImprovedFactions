package io.github.toberocat.improvedFactions.core.utils.command.exceptions;

import io.github.toberocat.improvedFactions.core.exceptions.TranslatableException;
import io.github.toberocat.improvedFactions.core.translator.Translatable;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Function;


public class CommandException extends TranslatableException {

    public CommandException(@NotNull String translationKey,
                            @NotNull Map<String, Function<Translatable, String>> placeholders) {
        super(translationKey, placeholders);
    }
}
