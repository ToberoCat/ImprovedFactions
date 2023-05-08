package io.github.toberocat.improvedfactions.spigot.handler.message.database.sql;

import io.github.toberocat.improvedFactions.core.translator.Translatable;
import io.github.toberocat.improvedfactions.spigot.handler.message.MessageHandler;
import org.apache.commons.lang.NotImplementedException;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class MySqlMessages implements MessageHandler {
    @Override
    public void sendMessage(@NotNull UUID player, @NotNull String message) {
        throw new NotImplementedException();
    }

    @Override
    public void sendMessage(@NotNull UUID player, @NotNull String query,
                            @NotNull Map<String, Function<Translatable, String>> placeholders) {

    }

}
