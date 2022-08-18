package io.github.toberocat.improvedfactions.spigot;

import io.github.toberocat.improvedFactions.core.registry.ImplementationHolder;
import io.github.toberocat.improvedFactions.core.utils.Logger;
import io.github.toberocat.improvedfactions.spigot.command.SpigotFactionCommand;
import io.github.toberocat.improvedfactions.spigot.handler.SpigotColorHandler;
import io.github.toberocat.improvedfactions.spigot.handler.SpigotConfigHandler;
import io.github.toberocat.improvedfactions.spigot.listener.PlayerJoinListener;
import io.github.toberocat.improvedfactions.spigot.listener.PlayerLeaveListener;
import io.github.toberocat.improvedfactions.spigot.listener.SpigotEventListener;
import io.github.toberocat.improvedfactions.spigot.plugin.ImprovedImplementation;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.List;

public final class MainIF extends JavaPlugin {

    @Override
    public void onEnable() {
        registerHandlers();
        registerCommands();

        try {
            ImplementationHolder.register();
        } catch (IOException e) {
            e.printStackTrace();
        }

        registerListener();
    }

    @Override
    public void onDisable() {
        ImplementationHolder.dispose();
    }

    private void registerListener() {
        List.of(
                new PlayerJoinListener(this),
                new PlayerLeaveListener(this)
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
