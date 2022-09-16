package io.github.toberocat.improvedfactions.spigot;

import io.github.toberocat.improvedFactions.core.registry.ImplementationHolder;
import io.github.toberocat.improvedFactions.core.utils.Logger;
import io.github.toberocat.improvedfactions.spigot.command.SpigotFactionCommand;
import io.github.toberocat.improvedfactions.spigot.handler.SpigotColorHandler;
import io.github.toberocat.improvedfactions.spigot.handler.SpigotConfigHandler;
import io.github.toberocat.improvedfactions.spigot.listener.PlayerLeaveListener;
import io.github.toberocat.improvedfactions.spigot.listener.PlayerMoveListener;
import io.github.toberocat.improvedfactions.spigot.listener.SpigotEventListener;
import io.github.toberocat.improvedfactions.spigot.listener.world.SpigotBlockListener;
import io.github.toberocat.improvedfactions.spigot.plugin.ImprovedImplementation;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.List;

public final class MainIF extends JavaPlugin {

    private final String currentVersion = "2.0.0-dev.3";
    private boolean enabledSuccessfully = false;

    @Override
    public void onEnable() {
        registerHandlers();
        try {
            ImplementationHolder.register();
        } catch (IOException e) {
            Logger.api().logException(e);
            Logger.api().logError("Couldn't load the entire ImprovedFactions backend. Disabling... ");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        enabledSuccessfully = true;

        registerCommands();
        registerListener();
        Logger.api().logInfo("§aLoaded ImprovedFactions with version §6%s", currentVersion);
    }

    @Override
    public void onDisable() {
        if (!enabledSuccessfully) return;

        // Check if the jar got replaced while classes where loaded
        try {
            Class.forName("io.github.toberocat.improvedFactions.core.utils.CheckerClass");
        } catch (ClassNotFoundException e) {
            Bukkit.getConsoleSender().sendMessage("[ImprovedFactions]§c Detected a change within the binaries. Saving will be skipped, because it would lead into many corrupted files");
            return;
        }

        ImplementationHolder.dispose();
    }

    private void registerListener() {
        List.of(
                new PlayerLeaveListener(this),
                new SpigotBlockListener(this),
                new PlayerMoveListener(this)
        ).forEach(SpigotEventListener::register);
    }

    private void registerHandlers() {
        ImplementationHolder.colorHandler = new SpigotColorHandler();
        ImplementationHolder.configHandler = new SpigotConfigHandler(getConfig());

        ImprovedImplementation implementation = new ImprovedImplementation(this);
        ImplementationHolder.improvedFactions = implementation;
        ImplementationHolder.logger = implementation;
    }

    private void registerCommands() {
        SpigotFactionCommand command = new SpigotFactionCommand();

        PluginCommand pluginCommand = getServer().getPluginCommand("faction");
        if (pluginCommand == null) {
            Logger.api().logError("Wasn't able to add command listeners");
            return;
        }

        pluginCommand.setTabCompleter(command);
        pluginCommand.setExecutor(command);
    }
}
