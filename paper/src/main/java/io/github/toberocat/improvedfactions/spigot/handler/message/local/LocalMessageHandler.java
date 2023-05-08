package io.github.toberocat.improvedfactions.spigot.handler.message.local;

import io.github.toberocat.improvedFactions.core.translator.Translatable;
import io.github.toberocat.improvedFactions.core.translator.Translation;
import io.github.toberocat.improvedFactions.core.utils.FileAccess;
import io.github.toberocat.improvedFactions.core.utils.Logger;
import io.github.toberocat.improvedfactions.spigot.MainIF;
import io.github.toberocat.improvedfactions.spigot.handler.message.MessageHandler;
import io.github.toberocat.improvedfactions.spigot.listener.SpigotEventListener;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;

import static io.github.toberocat.improvedfactions.spigot.player.SpigotFactionPlayer.PREFIX_QUERY;

public class LocalMessageHandler extends SpigotEventListener implements MessageHandler {
    // ToDo: Store the query path instead of an hardcoded translation file

    private final FileAccess fileAccess;
    private final Translation translation;

    public LocalMessageHandler(@NotNull MainIF plugin) {
        super(plugin);
        fileAccess = new FileAccess(plugin.getDataFolder(),
                "data", FileAccess.MESSAGES_FOLDER);
        translation = new Translation(Locale.US);

        register();
    }

    @EventHandler
    private void playerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        getMessages(player.getUniqueId()).forEach(m -> player.sendMessage(MiniMessage.miniMessage()
                .deserialize(m.message)));
    }

    private @NotNull LocalMessages getMessages(@NotNull UUID id) {
        try {
            return fileAccess.read(LocalMessages.class, id.toString());
        } catch (IOException e) {
            return new LocalMessages();
        }
    }

    private void addMessage(@NotNull UUID id, @NotNull String message) {
        LocalMessages messages = getMessages(id);
        messages.add(new Message(message, null));

        try {
            fileAccess.write(message, id.toString());
        } catch (IOException e) {
            Logger.api().logException(e);
        }
    }

    @Override
    public void sendMessage(@NotNull UUID player, @NotNull String message) {
        addMessage(player, translation.getMessage(PREFIX_QUERY, new HashMap<>()) + message);
    }

    @Override
    public void sendMessage(@NotNull UUID player, @NotNull String query,
                            @NotNull Map<String, Function<Translatable, String>> placeholders) {
        String msg = translation.getMessage(query, placeholders);
        if (msg != null)
            addMessage(player, msg);
    }

    protected static class LocalMessages extends ArrayList<Message> {

    }

    protected record Message(@NotNull String message, @Nullable String run) {

    }
}
