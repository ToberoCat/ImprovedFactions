package io.github.toberocat.improvedFactions.player;

import io.github.toberocat.improvedFactions.translator.Placeholder;
import io.github.toberocat.improvedFactions.translator.layout.Translatable;
import io.github.toberocat.improvedFactions.utils.ReturnConsumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface FactionPlayer<P> extends OfflineFactionPlayer<P> {

    @Nullable String getMessage(@NotNull ReturnConsumer<Translatable, String> query, Placeholder... placeholders);

    @Nullable String[] getMessageBatch(@NotNull ReturnConsumer<Translatable, String[]> query, Placeholder... placeholders);

    @NotNull String getLocal();
}
