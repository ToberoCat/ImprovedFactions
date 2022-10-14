package io.github.toberocat.improvedfactions.spigot;

import io.github.toberocat.improvedFactions.core.registry.ImplementationHolder;
import io.github.toberocat.improvedFactions.core.utils.Logger;
import io.github.toberocat.improvedfactions.spigot.command.SpigotFactionCommand;
import io.github.toberocat.improvedfactions.spigot.gui.provided.SpigotGuiImplementationManager;
import io.github.toberocat.improvedfactions.spigot.handler.SpigotActionHandler;
import io.github.toberocat.improvedfactions.spigot.handler.SpigotSoundHandler;
import io.github.toberocat.improvedfactions.spigot.handler.message.SpigotMessageHandler;
import io.github.toberocat.improvedfactions.spigot.handler.SpigotConfigHandler;
import io.github.toberocat.improvedfactions.spigot.item.SpigotItemHandler;
import io.github.toberocat.improvedfactions.spigot.listener.GuiListener;
import io.github.toberocat.improvedfactions.spigot.listener.PlayerLeaveListener;
import io.github.toberocat.improvedfactions.spigot.listener.PlayerMoveListener;
import io.github.toberocat.improvedfactions.spigot.listener.SpigotEventListener;
import io.github.toberocat.improvedfactions.spigot.listener.world.SpigotBlockListener;
import io.github.toberocat.improvedfactions.spigot.loom.BannerDesigner;
import io.github.toberocat.improvedfactions.spigot.placeholder.FactionExpansion;
import io.github.toberocat.improvedfactions.spigot.plugin.ImprovedImplementation;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public final class MainIF extends JavaPlugin {

    private final String currentVersion = "2.0.0-dev.4";
    private boolean enabledSuccessfully;

    @Override
    public void onEnable() {
        registerHandlers();
        try {
            ImplementationHolder.register();
        } catch (IOException | URISyntaxException e) {
            Logger.api().logException(e);
            Logger.api().logError("Couldn't load the entire ImprovedFactions backend. Disabling... ");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        enabledSuccessfully = true;

        registerCommands();
        registerListener();
        registerPapi();
        Logger.api().logInfo("§aLoaded ImprovedFactions with version §6%s", currentVersion);
    }

    @Override
    public void onDisable() {
        if (!enabledSuccessfully) return;

        // Check if the jar got replaced while classes where loaded
        try {
            Class.forName("io.github.toberocat.improvedFactions.core.utils.CheckerClass");
        } catch (ClassNotFoundException e) {
            Bukkit.getConsoleSender().sendMessage("[ImprovedFactions]§c Detected a change " +
                    "within the binaries. Saving will be skipped, because it would lead into many " +
                    "corrupted files");
            return;
        }

        BannerDesigner.dispose();
        ImplementationHolder.dispose();
    }

    private void registerListener() {
        List.of(
                new PlayerLeaveListener(this),
                new SpigotBlockListener(this),
                new PlayerMoveListener(this),
                new GuiListener(this)
        ).forEach(SpigotEventListener::register);
    }

    private void registerHandlers() {
        ImplementationHolder.messageHandler = new SpigotMessageHandler();
        ImplementationHolder.configHandler = new SpigotConfigHandler(getConfig(), "");
        ImplementationHolder.guiImplementation = new SpigotGuiImplementationManager();
        ImplementationHolder.itemHandler = new SpigotItemHandler();
        ImplementationHolder.soundHandler = new SpigotSoundHandler();
        ImplementationHolder.actionHandler = new SpigotActionHandler(this);

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

    private void registerPapi() {
        if (!Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            Logger.api().logWarning("To increase performance with ImprovedFactions, " +
                    "you should install PlaceHolderAPI for placeholder formatting");
            Logger.api().logInfo("§aUsing §6Integrated Placeholders§a for placeholder formatting");
            return;
        }

        new FactionExpansion().register();
        Logger.api().logInfo("§6Papi§a expansion registered");

    }

    public String getCurrentVersion() {
        return currentVersion;
    }
}
