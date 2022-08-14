package io.github.toberocat.improvedFactions.handler;

import io.github.toberocat.improvedFactions.exceptions.NoImplementationProvidedException;
import io.github.toberocat.improvedFactions.player.OfflineFactionPlayer;
import io.github.toberocat.improvedFactions.registry.ImplementationHolder;
import io.github.toberocat.improvedFactions.translator.Placeholder;
import org.jetbrains.annotations.NotNull;

public interface MessagingHandler {

    static @NotNull MessagingHandler api() {
        MessagingHandler implementation = ImplementationHolder.messagingHandler;
        if (implementation == null) throw new NoImplementationProvidedException("messaging handler");
        return implementation;
    }

    /**
     * Sends a message to the specified player
     * When the player is offline, the message will be sent as soon as they go online
     * in a world the plugin is allowed to operate on
     *
     * @param player The player to send the message to.
     * @param message The message to send to the player.
     */
    void sendMessage(@NotNull OfflineFactionPlayer<?> player, @NotNull String message);

    /**
     * Sends a message from the player's specific lang file
     * When the player is offline, the message will be sent as soon as they go online
     * in a world the plugin is allowed to operate on
     *
     * @param player The player to send the message to.
     * @param key The key of the translatable message.
     * @param placeholders The placeholders that will replace items in the string
     */
    void sendTranslatable(@NotNull OfflineFactionPlayer<?> player, @NotNull String key, Placeholder... placeholders);
}
