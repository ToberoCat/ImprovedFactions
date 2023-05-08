package io.github.toberocat.improvedFactions.core.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
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
    public static @Nullable String replace(@Nullable String string, @NotNull Map<String, String> placeholders) {
        if (string == null) {
            return null;
        }

        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            String key = entry.getKey();
            if (key == null) continue;

            String val = entry.getValue();
            if (val == null) continue;

            string = string.replace(key, val);
        }

        return string;
    }
}
