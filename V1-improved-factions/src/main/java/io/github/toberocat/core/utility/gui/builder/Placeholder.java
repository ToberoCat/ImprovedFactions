package io.github.toberocat.core.utility.gui.builder;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;

public class Placeholder {
    private LinkedHashMap<String, String> placeholders;

    public Placeholder() {
        this.placeholders = new LinkedHashMap<>();
    }

    public Placeholder placeholder(@NotNull String from, @NotNull String to) {
        placeholders.put(from, to);
        return this;
    }

    public LinkedHashMap<String, String> placeholders() {
        return placeholders;
    }
}
