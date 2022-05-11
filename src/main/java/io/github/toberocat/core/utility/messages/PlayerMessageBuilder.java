package io.github.toberocat.core.utility.messages;

import io.github.toberocat.core.utility.language.Language;
import net.md_5.bungee.api.chat.*;
import org.bukkit.entity.Player;


public class PlayerMessageBuilder {

    private final BaseComponent[] component;

    /**
     * This class got writen to make it easy to send hoverable & clickable messages.
     * Message layout: Use normal color formatting.
     * For hoverEvent use: {HOVER(index)}, where index an integer is. E.g: {HOVER(0)}. Index is used later for setting the event callback
     * For clickEvent use: {CLICK(index)}, where index an integer is. E.g: {CLICK(1)}. Note: Index should never be the same as another one. Used later for event callback
     *
     * @param message  The message. E.g: %&cHello;{HOVER}% &e&lworld %&c!;{CLICK(0)}{HOVER}%
     * @param commands Order the callbacks like you made the indexes. Index 0 in the () is equal to callbacks[0]
     */
    public PlayerMessageBuilder(String message, String... commands) {
        component = getComponent(message, commands);
    }

    private BaseComponent[] getComponent(String _message, String[] commands) {
        ComponentBuilder builder = new ComponentBuilder("");
        String message = Language.format(_message);

        String[] componentSplit = message.split("%");

        for (String component : componentSplit) {
            if (component == "r") builder.reset();
            else if (!component.contains(";")) {
                builder.append(TextComponent.fromLegacyText(component));
            } else {
                String[] split = component.split(";");
                String text = split[0];
                String[] events = split[1].split("\\{");

                for (String event : events) {
                    if (event.startsWith("HOVER")) {
                        builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                new ComponentBuilder("").append(TextComponent.fromLegacyText(text)).create()));
                    } else if (event.startsWith("CLICK")) {
                        builder.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                                commands[Integer.parseInt(event.split("\\(")[1].replaceAll("\\)}", ""))]));
                    }
                }
            }
        }

        return builder.create();
    }

    public void send(Player player) {
        player.spigot().sendMessage(component);
    }


}


