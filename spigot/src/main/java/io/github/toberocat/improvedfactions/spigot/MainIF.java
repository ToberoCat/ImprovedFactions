package io.github.toberocat.improvedfactions.spigot;

import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.player.OfflineFactionPlayer;
import io.github.toberocat.improvedFactions.core.registry.ImplementationHolder;
import io.github.toberocat.improvedFactions.core.world.World;
import io.github.toberocat.improvedfactions.spigot.handler.SpigotColorHandler;
import io.github.toberocat.improvedfactions.spigot.handler.SpigotConfigHandler;
import io.github.toberocat.improvedfactions.spigot.listener.PlayerJoinListener;
import io.github.toberocat.improvedfactions.spigot.listener.PlayerLeaveListener;
import io.github.toberocat.improvedfactions.spigot.listener.SpigotEventListener;
import io.github.toberocat.improvedfactions.spigot.player.SpigotFactionPlayer;
import io.github.toberocat.improvedfactions.spigot.player.SpigotOfflineFactionPlayer;
import io.github.toberocat.improvedfactions.spigot.world.SpigotWorld;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class MainIF extends JavaPlugin implements ImprovedFactions<org.bukkit.World> {

    @Override
    public void onEnable() {
        createFolders();
        registerListener();
        registerHandlers();

        try {
            ImplementationHolder.register();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        ImplementationHolder.improvedFactions = this;
    }

    private void createFolders() {

    }

    /* Implementation of ImprovedFactions */
    @Override
    public @Nullable FactionPlayer<?> getPlayer(@NotNull UUID id) {
        Player player = Bukkit.getPlayer(id);
        if (player == null) return null;
        return new SpigotFactionPlayer(player);
    }

    @Override
    public @Nullable FactionPlayer<?> getPlayer(@NotNull String name) {
        Player player = Bukkit.getPlayer(name);
        if (player == null) return null;
        return new SpigotFactionPlayer(player);
    }

    @Override
    public @NotNull OfflineFactionPlayer<?> getOfflinePlayer(@NotNull UUID id) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(id);
        return new SpigotOfflineFactionPlayer(player, this);
    }

    @Override
    public @Nullable OfflineFactionPlayer<?> getOfflinePlayer(@NotNull String name) {
        OfflinePlayer player = Arrays.stream(Bukkit.getOfflinePlayers())
                .filter(x -> name.equals(x.getName()))
                .findAny()
                .orElse(null);
        if (player == null) return null;

        return new SpigotOfflineFactionPlayer(player, this);
    }

    @Override
    public @Nullable World<org.bukkit.World> getWorld(@NotNull String name) {
        org.bukkit.World world = Bukkit.getWorld(name);
        if (world == null) return null;
        return new SpigotWorld(world);
    }

    @Override
    public @NotNull List<World<org.bukkit.World>> getAllWorlds() {
        return Bukkit.getWorlds().stream()
                .map(org.bukkit.World::getName)
                .map(this::getWorld)
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public @NotNull File getLocalFolder() {
        return new File(getDataFolder(), "Data");
    }

    @Override
    public @NotNull File getTempFolder() {
        return new File(getDataFolder(), ".temp");
    }

    @Override
    public @NotNull File getLangFolder() {
        return new File(getDataFolder(), "lang");
    }
}
