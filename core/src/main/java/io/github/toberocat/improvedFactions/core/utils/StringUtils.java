package io.github.toberocat.improvedFactions.core.utils;

import io.github.toberocat.improvedFactions.core.translator.Placeholder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    public static @Nullable String replaceAll(@Nullable String from, Placeholder... placeholders) {
        if (from == null) return null;

        String rep = from;
        for (Placeholder placeholder : placeholders)
            rep = rep.replaceAll(escape(placeholder.from()),
                            escape(placeholder.to()));

        return rep;
    }

    public static @Nullable String[] replaceAll(String[] from, Placeholder... placeholders) {
        if (from == null) return null;

        List<String> items = new LinkedList<>();
        for (String s : from) {
            for (Placeholder placeholder : placeholders) s = s.replaceAll(placeholder.from(), placeholder.to());
            items.add(s);
        }

        return items.toArray(String[]::new);
    }

    public static String escape(@NotNull String s) {
        return s.replace("\\", "\\\\")
                .replace("\t", "\\t")
                .replace("\b", "\\b")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\f", "\\f")
                .replace("'", "\\'")
                .replace("\"", "\\\"")
                .replace("{", "\\{")
                .replace("}", "\\}");
    }

    /**
     * Replaces a string with the provided placeholders.
     *
     * @param string        the string to replace
     * @param placeholders  the placeholders to replace the string with
     * @param caseSensitive whether the placeholders should be case-sensitive
     * @return the string replaced with the placeholders
     */
    @Contract("!null, _, _ -> !null")
    public static @Nullable String replace(@Nullable String string, @NotNull Map<String, String> placeholders, boolean caseSensitive) {
        if (string == null) {
            return null;
        }

        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            String key = entry.getKey();
            if (key == null) continue;

            String val = entry.getValue();
            if (val == null) continue;

            if (caseSensitive) {
                string = string.replace(key, val);
            } else {
                if (key.startsWith("{")) key = "\\{" + key.substring(1);
                Matcher match = Pattern.compile(key, Pattern.CASE_INSENSITIVE).matcher(string);
                while (match.find()) string = string.replace(match.group(), val);
            }

        }

        return string;
    }

    /**
     * Replaces a string with the provided placeholders.
     *
     * @param string       the string to replace
     * @param placeholders the placeholders to replace the string with
     * @return the string replaced with the placeholders
     */
    @Contract("!null, _ -> !null")
    public static @Nullable String replace(@Nullable String string, @NotNull Map<String, String> placeholders) {
        return replace(string, placeholders, false);
    }
}
