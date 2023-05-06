package io.github.toberocat.improvedFactions.core.handler.message;

import io.github.toberocat.improvedFactions.core.exceptions.NoImplementationProvidedException;
import io.github.toberocat.improvedFactions.core.player.OfflineFactionPlayer;
import io.github.toberocat.improvedFactions.core.registry.ImplementationHolder;
import org.jetbrains.annotations.NotNull;

public interface MessageHandler {

    static @NotNull MessageHandler api() {
        MessageHandler implementation = ImplementationHolder.messageHandler;
        if (implementation == null) throw new NoImplementationProvidedException("message handler");
        return implementation;
    }

    @NotNull String stripColor(@NotNull String text);

    @NotNull String format(@NotNull String text);

    @NotNull String format(@NotNull OfflineFactionPlayer player, @NotNull String text);
}
