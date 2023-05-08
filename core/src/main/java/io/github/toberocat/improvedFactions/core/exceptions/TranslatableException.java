package io.github.toberocat.improvedFactions.core.exceptions;

import io.github.toberocat.improvedFactions.core.translator.Translatable;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class TranslatableException extends Exception {

    private final @NotNull String translationKey;
    private final @NotNull Supplier<Map<String, Function<Translatable, String>>> placeholders;


    public TranslatableException(@NotNull String translationKey,
                                 @NotNull Supplier<Map<String, Function<Translatable, String>>> placeholders) {
        this.translationKey = translationKey;
        this.placeholders = placeholders;
    }

    public @NotNull String getTranslationKey() {
        return translationKey;
    }

    public @NotNull Map<String, Function<Translatable, String>> getPlaceholders() {
        return placeholders.get();
    }
}
