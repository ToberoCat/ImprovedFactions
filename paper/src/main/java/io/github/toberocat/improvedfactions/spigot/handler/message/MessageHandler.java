package io.github.toberocat.improvedfactions.spigot.handler.message;

import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.translator.Translatable;
import io.github.toberocat.improvedfactions.spigot.MainIF;
import io.github.toberocat.improvedfactions.spigot.handler.message.database.sql.MySqlMessages;
import io.github.toberocat.improvedfactions.spigot.handler.message.local.LocalMessageHandler;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public interface MessageHandler {

    MessageHandler api = createApi();

    private static @NotNull MessageHandler createApi() {
        // Support proper plugins having mails
        if (ImprovedFactions.api().getConfig().getBool("storage.use-mysql", false))
            return new MySqlMessages();
        return new LocalMessageHandler(JavaPlugin.getPlugin(MainIF.class));
    }

    void sendMessage(@NotNull UUID player,
                     @NotNull String message);

    void sendMessage(@NotNull UUID player,
                     @NotNull String query,
                     @NotNull Map<String, Function<Translatable, String>> placeholders);
}
