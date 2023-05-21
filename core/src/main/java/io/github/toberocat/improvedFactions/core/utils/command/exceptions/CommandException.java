package io.github.toberocat.improvedFactions.core.utils.command.exceptions;

import io.github.toberocat.improvedFactions.core.exceptions.TranslatableException;
import io.github.toberocat.improvedFactions.core.translator.Translatable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;


public class CommandException extends TranslatableException {

    public CommandException(@NotNull String translationKey,
                            @NotNull Supplier<Map<String, Function<Translatable, String>>> placeholders) {
        super(translationKey, placeholders);
    }

    public CommandException(@NotNull String translationKey) {
        this(translationKey, HashMap::new);
    }

    public CommandException(@NotNull TranslatableException e) {
        this(e.getTranslationKey(), e.getSupplier());
    }
}
