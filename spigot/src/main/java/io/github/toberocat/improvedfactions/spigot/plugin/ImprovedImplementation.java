package io.github.toberocat.improvedfactions.spigot.plugin;

import io.github.toberocat.improvedFactions.core.handler.ConfigHandler;
import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.handler.component.PlayerLister;
import io.github.toberocat.improvedFactions.core.handler.component.Scheduler;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.player.OfflineFactionPlayer;
import io.github.toberocat.improvedFactions.core.utils.Logger;
import io.github.toberocat.improvedfactions.spigot.MainIF;
import io.github.toberocat.improvedfactions.spigot.handler.SpigotConfigHandler;
import io.github.toberocat.improvedfactions.spigot.player.SpigotFactionPlayer;
import io.github.toberocat.improvedfactions.spigot.player.SpigotOfflineFactionPlayer;
import io.github.toberocat.improvedfactions.spigot.scheduler.SpigotScheduler;
import io.github.toberocat.improvedfactions.spigot.utils.YamlLoader;
import io.github.toberocat.improvedfactions.spigot.world.SpigotWorld;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Stream;

public class ImprovedImplementation implements ImprovedFactions<World>, Logger {

    private final MainIF plugin;
    private final Scheduler scheduler;
    private final java.util.logging.Logger logger;


    public ImprovedImplementation(MainIF plugin) {
        this.plugin = plugin;
        this.scheduler = new SpigotScheduler(plugin);
        this.logger = plugin.getLogger();
    }

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
        return new SpigotOfflineFactionPlayer(player, plugin);
    }

    @Override
    public @Nullable OfflineFactionPlayer<?> getOfflinePlayer(@NotNull String name) {
        OfflinePlayer player = Arrays.stream(Bukkit.getOfflinePlayers())
                .filter(x -> name.equals(x.getName()))
                .findAny()
                .orElse(null);
        if (player == null) return null;

        return new SpigotOfflineFactionPlayer(player, plugin);
    }

    @Override
    public @NotNull PlayerLister<Player, OfflinePlayer> listPlayers() {
        return new PlayerLister<>() {
            @Override
            public @NotNull Stream<UUID> getPlayers() {
                return Arrays.stream(Bukkit.getOfflinePlayers())
                        .map(OfflinePlayer::getUniqueId);
            }

            @Override
            public @NotNull Stream<UUID> getOnlinePlayers() {
                return Bukkit.getOnlinePlayers()
                        .stream()
                        .map(Player::getUniqueId);
            }

            @Override
            public @NotNull Stream<String> getPlayerNames() {
                return Arrays.stream(Bukkit.getOfflinePlayers())
                        .map(OfflinePlayer::getName)
                        .filter(Objects::nonNull);
            }

            @Override
            public @NotNull Stream<String> getOnlinePlayerNames() {
                return Bukkit.getOnlinePlayers()
                        .stream()
                        .map(Player::getName);
            }

            @Override
            public @NotNull List<OfflinePlayer> getRawOfflinePlayers() {
                return Arrays.stream(Bukkit.getOfflinePlayers()).toList();
            }

            @Override
            public @NotNull List<Player> getRawOnlinePlayers() {
                return Collections.unmodifiableList(Bukkit.getOnlinePlayers().stream().toList());
            }
        };
    }

    @Override
    public @NotNull Scheduler getScheduler() {
        return scheduler;
    }

    @Override
    public @Nullable io.github.toberocat.improvedFactions.core.world.World<World> getWorld(@NotNull String name) {
        org.bukkit.World world = Bukkit.getWorld(name);
        if (world == null) return null;
        return new SpigotWorld(world);
    }

    @Override
    public @NotNull List<io.github.toberocat.improvedFactions.core.world.World<World>> getAllWorlds() {
        return Bukkit.getWorlds().stream()
                .map(org.bukkit.World::getName)
                .map(this::getWorld)
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public @NotNull File getLocalDataFolder() {
        return new File(getDataFolder(), "data");
    }

    @Override
    public @NotNull File getDataFolder() {
        return plugin.getDataFolder();
    }

    @Override
    public @NotNull File getTempFolder() {
        return new File(getDataFolder(), ".temp");
    }

    @Override
    public @NotNull File getLangFolder() {
        return new File(getDataFolder(), "lang");
    }

    @Override
    public @NotNull ConfigHandler getConfig(@NotNull String relativePath) {
        return new SpigotConfigHandler(new YamlLoader(new File(getDataFolder(), relativePath), plugin)
                .logger(logger)
                .load()
                .fileConfiguration());
    }

    @Override
    public void logInfo(@NotNull String message, Object... plObjects) {
        logger.log(Level.INFO, String.format(message, plObjects));
    }

    @Override
    public void logWarning(@NotNull String message) {
        logger.log(Level.WARNING, message);
    }

    @Override
    public void logError(@NotNull String message) {
        logger.log(Level.SEVERE, message);
    }

    @Override
    public void logException(@NotNull Exception e) {
        e.printStackTrace();
    }
}
