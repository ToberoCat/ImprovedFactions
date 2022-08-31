package io.github.toberocat.improvedfactions.spigot.handler;

import io.github.toberocat.improvedFactions.core.handler.ConfigHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record SpigotConfigHandler(@NotNull FileConfiguration config) implements ConfigHandler {

    @Override
    public @Nullable
    String getString(@NotNull String path) {
        return config.getString(path);
    }

    @Override
    public @NotNull
    String getString(@NotNull String path, @NotNull String def) {
        return config.getString(path, def);
    }

    @Override
    public @NotNull
    List<String> getList(@NotNull String path) {
        return config.getStringList(path);
    }

    @Override
    public int getInt(@NotNull String path) {
        return config.getInt(path);
    }

    @Override
    public int getInt(@NotNull String path, int def) {
        return config.getInt(path, def);
    }

    @Override
    public long getLong(@NotNull String path) {
        return config.getLong(path);
    }

    @Override
    public long getLong(@NotNull String path, long def) {
        return config.getLong(path, def);
    }

    @Override
    public boolean getBool(@NotNull String path) {
        return config.getBoolean(path);
    }

    @Override
    public boolean getBool(@NotNull String path, boolean def) {
        return config.getBoolean(path, def);
    }
}
