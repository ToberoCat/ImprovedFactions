package io.github.toberocat.improvedfactions.spigot.handler;

import io.github.toberocat.improvedFactions.core.handler.ConfigFile;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public record SpigotConfigFile(@NotNull FileConfiguration config, @NotNull String root) implements ConfigFile {

    @Override
    public @Nullable
    String getString(@NotNull String path) {
        return config.getString(root + path);
    }

    @Override
    public @NotNull
    String getString(@NotNull String path, @NotNull String def) {
        return config.getString(root + path, def);
    }

    @Override
    public @NotNull
    List<String> getList(@NotNull String path) {
        return config.getStringList(root + path);
    }

    @Override
    public @NotNull List<String> getSubSections(@NotNull String section) {
        ConfigurationSection cSection = config.getConfigurationSection(root + section);
        if (cSection == null) return Collections.emptyList();
        return cSection.getKeys(false).stream().toList();
    }

    @Override
    public int getInt(@NotNull String path) {
        return config.getInt(root + path);
    }

    @Override
    public int getInt(@NotNull String path, int def) {
        return config.getInt(root + path, def);
    }

    @Override
    public long getLong(@NotNull String path) {
        return config.getLong(root + path);
    }

    @Override
    public long getLong(@NotNull String path, long def) {
        return config.getLong(root + path, def);
    }

    @Override
    public boolean getBool(@NotNull String path) {
        return config.getBoolean(root + path);
    }

    @Override
    public boolean getBool(@NotNull String path, boolean def) {
        return config.getBoolean(root + path, def);
    }

    @Override
    public boolean exists(@NotNull String path) {
        return config.contains(path);
    }

    @Override
    public @NotNull ConfigFile getSection(@NotNull String path) {
        return new SpigotConfigFile(config, path + ".");
    }
}
