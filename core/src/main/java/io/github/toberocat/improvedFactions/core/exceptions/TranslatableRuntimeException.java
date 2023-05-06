package io.github.toberocat.improvedFactions.core.exceptions;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class TranslatableRuntimeException extends RuntimeException {
    private final @NotNull String translationKey;
    private final @NotNull Map<String, String> placeholders;


    public TranslatableRuntimeException(@NotNull String translationKey, @NotNull Map<String, String> placeholders) {
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
