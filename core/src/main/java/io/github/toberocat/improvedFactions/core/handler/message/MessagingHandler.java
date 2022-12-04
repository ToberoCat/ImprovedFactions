package io.github.toberocat.improvedFactions.core.handler.message;

import io.github.toberocat.improvedFactions.core.exceptions.NoImplementationProvidedException;
import io.github.toberocat.improvedFactions.core.registry.ImplementationHolder;
import io.github.toberocat.improvedFactions.core.player.OfflineFactionPlayer;
import io.github.toberocat.improvedFactions.core.translator.Placeholder;
import org.jetbrains.annotations.NotNull;

public interface MessagingHandler { // ToDo: Remove this and embbed it directly into the offline sender, as that's the only place where needed

    static @NotNull MessagingHandler api() {
        MessagingHandler implementation = ImplementationHolder.messagingHandler;
        if (implementation == null) throw new NoImplementationProvidedException("messaging handler");
        return implementation;
    }

    /**
     * Sends a message to the specified sender
     * When the sender is offline, the message will be sent as soon as they go online
     * in a world the plugin is allowed to operate on
     *
     * @param player The sender to send the message to.
     * @param message The message to send to the sender.
     */
    void sendMessage(@NotNull OfflineFactionPlayer<?> player, @NotNull String message);

    /**
     * Sends a message from the sender's specific lang file
     * When the sender is offline, the message will be sent as soon as they go online
     * in a world the plugin is allowed to operate on
     *
     * @param player The sender to send the message to.
     * @param key The key of the translatable message.
     * @param placeholders The placeholders that will replace items in the string
     */
    void sendTranslatable(@NotNull OfflineFactionPlayer<?> player, @NotNull String key, Placeholder... placeholders);
}
