package io.github.toberocat.improvedfactions.spigot.handler.message;

import io.github.toberocat.improvedFactions.core.handler.ConfigHandler;
import io.github.toberocat.improvedFactions.core.translator.Placeholder;
import io.github.toberocat.improvedFactions.core.translator.layout.Translatable;
import io.github.toberocat.improvedfactions.spigot.MainIF;
import io.github.toberocat.improvedfactions.spigot.handler.message.database.sql.MySqlMessages;
import io.github.toberocat.improvedfactions.spigot.handler.message.local.LocalMessageHandler;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.Function;

public interface MessageHandler {

    MessageHandler api = createApi();

    private static @NotNull MessageHandler createApi() {
        if (ConfigHandler.api().getBool("storage.use-mysql", false))
            return new MySqlMessages();
        return new LocalMessageHandler(JavaPlugin.getPlugin(MainIF.class));
    }

    /**
     * @deprecated Use {@link MessageHandler#sendFancyMessage(UUID, Function, Placeholder...)} instead
     */
    @Deprecated
    void sendMessage(@NotNull UUID player, @NotNull String message);

    /**
     * @deprecated Use {@link MessageHandler#sendFancyMessage(UUID, Function, Placeholder...)} instead
     */
    @Deprecated
    void sendTranslatable(@NotNull UUID player,
                          @NotNull Function<Translatable, String> query,
                          Placeholder... placeholders);

    /**
     * @deprecated Use {@link MessageHandler#sendFancyMessage(UUID, Function, Placeholder...)} instead
     */
    @Deprecated
    void sendClickableTranslatable(@NotNull UUID player,
                                   @NotNull Function<Translatable, String> query,
                                   @NotNull String command,
                                   Placeholder... placeholders);

    void sendFancyMessage(@NotNull UUID player,
                          @NotNull Function<Translatable, String> query,
                          Placeholder... placeholders);
}
