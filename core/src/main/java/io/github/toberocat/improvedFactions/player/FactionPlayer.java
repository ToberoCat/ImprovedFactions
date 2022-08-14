package io.github.toberocat.improvedFactions.player;

import io.github.toberocat.improvedFactions.translator.Placeholder;
import org.jetbrains.annotations.NotNull;

public interface FactionPlayer<P> extends OfflineFactionPlayer<P> {

    @NotNull String getMessage(@NotNull String key, Placeholder... placeholders);

    @NotNull String[] getMessageBatch(@NotNull String parentNode, Placeholder... placeholders);

    @NotNull String getLocal();
}
