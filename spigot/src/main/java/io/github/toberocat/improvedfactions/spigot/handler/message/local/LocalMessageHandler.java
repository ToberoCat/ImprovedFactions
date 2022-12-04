package io.github.toberocat.improvedfactions.spigot.handler.message.local;

import io.github.toberocat.improvedFactions.core.translator.Placeholder;
import io.github.toberocat.improvedFactions.core.translator.Translation;
import io.github.toberocat.improvedFactions.core.translator.layout.Translatable;
import io.github.toberocat.improvedFactions.core.utils.FileAccess;
import io.github.toberocat.improvedFactions.core.utils.Logger;
import io.github.toberocat.improvedfactions.spigot.MainIF;
import io.github.toberocat.improvedfactions.spigot.handler.message.MessageHandler;
import io.github.toberocat.improvedfactions.spigot.listener.SpigotEventListener;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.function.Function;

import static io.github.toberocat.improvedfactions.spigot.player.SpigotFactionPlayer.PREFIX_QUERY;

public class LocalMessageHandler extends SpigotEventListener implements MessageHandler { // ToDo: Store the query path instead of an hardcoded translation file

    private final FileAccess fileAccess;
    private final Translation translation;

    public LocalMessageHandler(@NotNull MainIF plugin) {
        super(plugin);
        fileAccess = new FileAccess(plugin.getDataFolder(),
                "data", FileAccess.MESSAGES_FOLDER);
        translation = new Translation("en_us");

        register();
    }

    @EventHandler
    private void playerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        getMessages(player.getUniqueId()).forEach(m -> {
            if (m.run == null) {
                player.sendMessage(m.message);
            } else {
                TextComponent component = new TextComponent(TextComponent.fromLegacyText(
                        ChatColor.translateAlternateColorCodes('&', m.message)));
                component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + m.run));
                player.spigot().sendMessage(component);
            }
        });
    }

    private @NotNull LocalMessages getMessages(@NotNull UUID id) {
        try {
            return fileAccess.read(LocalMessages.class, id.toString());
        } catch (IOException e) {
            return new LocalMessages();
        }
    }

    private void addMessage(@NotNull UUID id, @NotNull String message, @Nullable String run) {
        LocalMessages messages = getMessages(id);
        messages.add(new Message(message, run));

        try {
            fileAccess.write(message, id.toString());
        } catch (IOException e) {
            Logger.api().logException(e);
        }
    }

    @Override
    public void sendMessage(@NotNull UUID player,
                            @NotNull String message) {
        addMessage(player, message, null);
    }

    @Override
    public void sendTranslatable(@NotNull UUID player,
                                 @NotNull Function<Translatable, String> query,
                                 Placeholder... placeholders) {
        String msg = translation.getMessage(query);
        if (msg != null) addMessage(player, translation.getMessage(PREFIX_QUERY) + msg, null);
    }

    @Override
    public void sendClickableTranslatable(@NotNull UUID player,
                                          @NotNull Function<Translatable, String> query,
                                          @NotNull String command, Placeholder... placeholders) {
        String msg = translation.getMessage(query);
        if (msg != null) addMessage(player, translation.getMessage(PREFIX_QUERY) + msg, command);
    }

    @Override
    public void sendFancyMessage(@NotNull UUID player, @NotNull Function<Translatable, String> query, Placeholder... placeholders) {
        String msg = translation.getMessage(query);
        if (msg != null)
            addMessage(player, translation.getMessage(PREFIX_QUERY) + msg, null);
    }

    protected static class LocalMessages extends ArrayList<Message> {

    }

    protected static record Message(@NotNull String message, @Nullable String run) {

    }
}
