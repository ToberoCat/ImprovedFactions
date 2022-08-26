package io.github.toberocat.improvedFactions.core.utils;

import io.github.toberocat.improvedFactions.core.translator.Placeholder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

public class StringUtils {
    public static @Nullable String replaceAll(final String from, Placeholder... placeholders) {
        if (from == null) return null;

        String rep = from;
        for (Placeholder placeholder : placeholders)
            rep =
                    rep.replaceAll(escape(placeholder.from()),
                            escape(placeholder.to()));

        return rep;
    }

    public static @Nullable String[] replaceAll(final String[] from, Placeholder... placeholders) {
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
}
