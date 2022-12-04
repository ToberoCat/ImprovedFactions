package io.github.toberocat.improvedfactions.spigot.utils;

import io.github.toberocat.improvedFactions.core.handler.message.MessageHandler;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FancyMessage {
    private static final Pattern ATTRIBUTE_PATTERN = Pattern.compile("\\{[A-z]{1,5}:[^{}]+}|.");
    private final String raw;
    private ComponentBuilder builder;

    /**
     * Examples:
     */
    public FancyMessage(String raw) {
        this.raw = raw;
        System.out.println(raw);
        format();
    }

    /**
     * Send the message to a sender
     *
     * @param player
     */
    public void send(Player player) {
        player.spigot().sendMessage(builder.create());
    }

    private void format() {
        this.builder = new ComponentBuilder();
        Matcher matcher = ATTRIBUTE_PATTERN.matcher(raw);

        while (matcher.find()) {
            String match = matcher.group(0);

            if (match.matches("\\{[^{}]+}")) {
                HashMap<String, String> attributes = this.getAttributes(match.substring(1, match.length() - 1));

                for (String key : attributes.keySet()) {
                    System.out.println(key + " : " + attributes.get(key));
                }

                BaseComponent component = new TextComponent(color(attributes.get("text")));

                if (attributes.containsKey("hover")) {
                    component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(color(attributes.get("hover")))));
                }

                if (attributes.containsKey("url")) {
                    component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, attributes.get("url")));
                }

                if (attributes.containsKey("suggest_command")) {
                    component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, attributes.get("suggest_command")));
                }

                if (attributes.containsKey("command")) {
                    component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, attributes.get("command")));
                }

                builder.append(component);
            } else {
                builder.append(match);
            }
        }
    }

    private HashMap<String, String> getAttributes(String string) {
        HashMap<String, String> map = new HashMap<>();
        String[] attributes = string.split("[ ]?[;][ ]?");

        for (String attr : attributes) {
            String[] finalSplit = attr.split(":", 2);
            map.put(finalSplit[0], finalSplit[1]);
        }

        return map;
    }

    private String color(String string) {
        return MessageHandler.api().format(string);
    }
}