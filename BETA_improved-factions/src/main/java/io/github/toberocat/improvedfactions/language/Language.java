package io.github.toberocat.improvedfactions.language;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.toberocat.improvedfactions.ImprovedFactionsMain;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Language {
    private static final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");

    private static Map<String, LangMessage> langFiles;


    public static void init(ImprovedFactionsMain plugin, File langDir) {
        langFiles = new HashMap<>();

        for (File file : langDir.listFiles()) {
            String[] split = file.getName().split("\\.");
            if (split.length > 1 && split[1].equals("lang")) {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    LangMessage langMessage = mapper.readValue(file, LangMessage.class);
                    langFiles.put(split[0], langMessage);
                } catch (IOException e) {
                    plugin.getServer().getConsoleSender().sendMessage("§7[Factions] §cIgnoring " + file.getName() + ". " + e.getMessage());
                }
            }
        }
    }

    public static boolean hasMessage(String msgKey, Player player) {
        String locale = player.getLocale();
        if (langFiles.containsKey(locale)) {
            LangMessage langMessage = langFiles.get(locale);

            return langMessage.getMessages().containsKey(msgKey);
        } else {
            LangMessage langMessage = langFiles.get("en_us");

            return langMessage.getMessages().containsKey(msgKey);
        }
    }

    public static String getMessage(String msgKey, Player player) {
        String locale = player.getLocale();
        if (langFiles.containsKey(locale)) {
            LangMessage langMessage = langFiles.get(locale);

           return getPrefix() + format(langMessage.getMessages().get(msgKey));
        } else {
            LangMessage langMessage = langFiles.get("en_us");

            return getPrefix() + format(langMessage.getMessages().get(msgKey));
        }
    }

    public static void sendMessage(String msgKey, Player player, Parseable... parseables) {
        String locale = player.getLocale();
        LangMessage langMessage = null;

        if (langFiles == null) {
            player.sendMessage("Error: lang files didn't get initialised. Report it to the ImprovedFactions dev or the server owner");
            return;
        }

        if (langFiles.containsKey(locale)) {
            langMessage = langFiles.get(locale);
        } else {
            langMessage = langFiles.get("en_us");
        }

        if (langMessage.getMessages().containsKey(msgKey)) {
            player.sendMessage(getPrefix() + format(parse(langMessage.getMessages().get(msgKey), parseables)));
        }
    }

    public static String parse(String message, Parseable[] parseables) {
        if (parseables == null) return message;

        for (Parseable parseable : parseables) {
            if (parseable != null) {
                message = message.replaceAll(escape(parseable.getParse()), escape(parseable.getTo()));
            }
        }
        return message;
    }

    public static String getPrefix() {
        return format(ImprovedFactionsMain.getPlugin()
                        .getLanguageData().getConfig().getString("prefix") + " §f");
    }

    public static String format(String msg) {
        if (Bukkit.getVersion().contains("1.16") || Bukkit.getVersion().contains("1.17") || Bukkit.getVersion().contains("1.18")) {
            Matcher matcher = pattern.matcher(msg);
            while (matcher.find()) {
                String color = msg.substring(matcher.start(), matcher.end());
                msg = msg.replace(color, ChatColor.of(color) + "");
                matcher = pattern.matcher(msg);
            }
        }
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static String escape(String s){
        return s.replace("\\", "\\\\")
                .replace("\t", "\\t")
                .replace("\b", "\\b")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\f", "\\f")
                .replace("\'", "\\'")
                .replace("\"", "\\\"")
                .replace("{", "\\{")
                .replace("}", "\\}");
    }

    public static void sendRawMessage(String message, Player player) {
        player.sendMessage(getPrefix() + format(message));
    }
}
