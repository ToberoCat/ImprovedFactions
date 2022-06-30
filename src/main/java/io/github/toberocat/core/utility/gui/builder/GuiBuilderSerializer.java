package io.github.toberocat.core.utility.gui.builder;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.github.toberocat.core.utility.Utility.replace;

public class GuiBuilderSerializer {

    private final ConfigurationSection section;
    private final LinkedHashMap<String, String> placeholders;

    public GuiBuilderSerializer(@NotNull ConfigurationSection section, @NotNull LinkedHashMap<String, String> placeholders) {
        this.section = section;
        this.placeholders = placeholders;
    }

    public Set<String> readKeys(String path) {
        if (!section.contains(path)) return new LinkedHashSet<>();

        ConfigurationSection section = this.section.getConfigurationSection(path);
        if (section == null) return new LinkedHashSet<>();
        return section.getKeys(false);
    }

    public List<String> readList(String path, List<String> def) {
        if (!section.contains(path)) return def;
        return section.getStringList(path);
    }

    public boolean readBool(String path, boolean def) {
        if (!section.contains(path)) return def;
        return section.getBoolean(path);
    }

    public String readString(String path, String def) {
        if (!section.contains(path)) return def;
        String val = section.getString(path);
        if (val == null) val = def;

        return replace(val, placeholders, false);
    }

    public int readInt(String path, int def) {
        if (!section.contains(path)) return def;
        return section.getInt(path);
    }
}
