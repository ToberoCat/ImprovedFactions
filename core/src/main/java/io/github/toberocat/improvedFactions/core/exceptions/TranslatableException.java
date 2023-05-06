package io.github.toberocat.improvedFactions.core.exceptions;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class TranslatableException extends Exception {

    private final @NotNull String translationKey;
    private final @NotNull Map<String, String> placeholders;


    public TranslatableException(@NotNull String translationKey, @NotNull Map<String, String> placeholders) {
        this.translationKey = translationKey;
        this.placeholders = placeholders;
    }

    public @NotNull String getTranslationKey() {
        return translationKey;
    }

    public @NotNull Map<String, String> getPlaceholders() {
        return placeholders;
    }
}
