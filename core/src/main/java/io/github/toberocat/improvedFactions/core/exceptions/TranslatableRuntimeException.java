package io.github.toberocat.improvedFactions.core.exceptions;

import org.jetbrains.annotations.NotNull;

public class TranslatableRuntimeException extends RuntimeException {
    private final @NotNull String translationKey;

    public TranslatableRuntimeException(@NotNull String translationKey) {
        this.translationKey = translationKey;
    }

    public @NotNull String getTranslationKey() {
        return translationKey;
    }
}
