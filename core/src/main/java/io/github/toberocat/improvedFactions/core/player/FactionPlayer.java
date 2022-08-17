package io.github.toberocat.improvedFactions.core.player;

import io.github.toberocat.improvedFactions.core.utils.ReturnConsumer;
import io.github.toberocat.improvedFactions.core.translator.Placeholder;
import io.github.toberocat.improvedFactions.core.translator.layout.Translatable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface FactionPlayer<P> extends OfflineFactionPlayer<P> {

    @Nullable String getMessage(@NotNull ReturnConsumer<Translatable, String> query, Placeholder... placeholders);

    @Nullable String[] getMessageBatch(@NotNull ReturnConsumer<Translatable, String[]> query, Placeholder... placeholders);

    @NotNull String getLocal();
}
