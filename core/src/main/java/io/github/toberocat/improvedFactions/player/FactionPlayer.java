package io.github.toberocat.improvedFactions.player;

import io.github.toberocat.improvedFactions.translator.Placeholder;
import io.github.toberocat.improvedFactions.translator.layout.Translatable;
import io.github.toberocat.improvedFactions.utils.ReturnConsumer;
import org.jetbrains.annotations.NotNull;

public interface FactionPlayer<P> extends OfflineFactionPlayer<P> {

    @NotNull String getMessage(@NotNull ReturnConsumer<Translatable, String> query, Placeholder... placeholders);

    @NotNull String[] getMessageBatch(@NotNull ReturnConsumer<Translatable, String[]> query, Placeholder... placeholders);

    @NotNull String getLocal();
}
