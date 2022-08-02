package io.github.toberocat.improvedFactions.player;

import io.github.toberocat.improvedFactions.translator.Placeholder;
import org.jetbrains.annotations.NotNull;

public interface FactionPlayer<P> extends OfflineFactionPlayer<P> {
    void sendMessage(@NotNull String message);
    void sendTranslatable(@NotNull String key, Placeholder... placeholders);
}
