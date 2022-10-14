package io.github.toberocat.improvedfactions.spigot.utils;

import io.github.toberocat.improvedFactions.core.utils.FileUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class YamlLoader {

    private final Plugin plugin;
    private final File file;

    private Logger logger;
    private Player player;
    private String resource;

    private boolean loaded;
    private boolean success;
    private boolean invalidConfiguration;

    private FileConfiguration fileConfiguration;

    public YamlLoader(@NotNull File file, @NotNull Plugin plugin) {
        this.plugin = plugin;
        this.file = file;
    }

    public YamlLoader(@NotNull String fileName, @NotNull Plugin plugin) {
        this(new File(plugin.getDataFolder(), fileName), plugin);
    }

    public @NotNull File file() {
        return file;
    }

    public @NotNull String name() {
        return file.getName();
    }

    /* Settings */

    private @NotNull Logger logger() {
        return (logger == null) ? plugin.getLogger() : logger;
    }

    public @NotNull YamlLoader logger(@Nullable Logger logger) {
        if (logger != null) {
            this.logger = logger;
        }

        return this;
    }

    public @NotNull YamlLoader logger(@Nullable Plugin plugin) {
        if (plugin != null) {
            logger(plugin.getLogger());
        }

        return this;
    }

    public @NotNull YamlLoader player(@Nullable Player player) {
        if (player != null) {
            this.player = player;
        }

        return this;
    }

    public @NotNull YamlLoader resource(@NotNull String name) {
        resource = name;
        return this;
    }

    public @NotNull YamlLoader resource() {
        resource(file.getName());
        return this;
    }

    /* Load */

    public final @NotNull YamlLoader load() {
        if (loaded) {
            throw new IllegalStateException(getClass().getSimpleName() + " has already been used");
        }

        loaded = true;
        internalLoad();
        return this;
    }

    public void error(@NotNull String message) {
        message = message.replace("{name}", "\"" + name() + "\"");

        logger().warning(message);

        if (player != null) {
            player.sendMessage(ChatColor.RED + message);
        }
    }

    private void internalLoad() {

        /* Create */

        File dataFolder = plugin.getDataFolder();

        if (!file.exists()) {

            /* Parent */

            File parent = file.getParentFile();

            if (!parent.exists()) {
                boolean success = parent.mkdirs();

                if (!success) {
                    error("Could not create the necessary directories for the file {name}");
                    return;
                }
            }

            /* Child */

            if (resource == null) {

                /* Create File */

                boolean suc;

                try {
                    suc = file.createNewFile();
                } catch (IOException e) {
                    error("Could not create file \"" + name() + "\"");
                    e.printStackTrace();
                    return;
                }

                if (!suc) {
                    error("Attempted to create file \"" + name() + "\" which already existed");
                    return;
                }
            } else {

                /* Save Resource */

                plugin.saveResource(resource, false);
                File resourceFile = new File(dataFolder, resource);

                if (!resourceFile.equals(file)) {

                    /* Move */

                    try {
                        FileUtils.move(resourceFile, file);
                    } catch (IOException e) {
                        error("Could not move created resource \"" + resourceFile + "\" to desired destination");
                        e.printStackTrace();
                        return;
                    }
                }
            }
        }

        /* Load */

        YamlConfiguration config = new YamlConfiguration();

        try {
            config.load(file);
        } catch (InvalidConfigurationException e) {
            error("Invalid YML formatting in \"" + name() + "\"");
            invalidConfiguration = true;
            e.printStackTrace();
            return;
        } catch (Exception e) {
            error("Could not load file \"" + name() + "\"");
            e.printStackTrace();
            return;
        }

        success = true;
        fileConfiguration = config;
    }

    /* Results */

    protected void check() {
        if (!loaded) {
            throw new IllegalStateException(getClass().getSimpleName() + " has not been used to load configuration yet");
        }
    }

    public @NotNull FileConfiguration fileConfiguration() {
        check();
        return (fileConfiguration == null) ? new YamlConfiguration() : fileConfiguration;
    }

    public boolean success() {
        check();
        return success;
    }

    public boolean invalidConfiguration() {
        check();
        return invalidConfiguration;
    }
}
