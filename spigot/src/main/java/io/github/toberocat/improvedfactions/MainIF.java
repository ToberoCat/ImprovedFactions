package io.github.toberocat.improvedfactions;

import io.github.toberocat.improvedFactions.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.player.FactionPlayer;
import io.github.toberocat.improvedFactions.player.OfflineFactionPlayer;
import io.github.toberocat.improvedFactions.registry.ImplementationHolder;
import io.github.toberocat.improvedFactions.world.World;
import io.github.toberocat.improvedfactions.listener.PlayerJoinListener;
import io.github.toberocat.improvedfactions.listener.PlayerLeaveListener;
import io.github.toberocat.improvedfactions.listener.SpigotEventListener;
import io.github.toberocat.improvedfactions.player.SpigotFactionPlayer;
import io.github.toberocat.improvedfactions.world.SpigotWorld;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;

public final class MainIF extends JavaPlugin implements ImprovedFactions {

    private static MainIF plugin;
    private final Map<String, NamespacedKey> NAMESPACED_KEY_MAP = new LinkedHashMap<>();

    public static MainIF getPlugin() {
        return plugin;
    }

    public @NotNull NamespacedKey getNamespacedKey(@NotNull String key) {
        if (!NAMESPACED_KEY_MAP.containsKey(key))
            NAMESPACED_KEY_MAP.put(key, new NamespacedKey(plugin, key));

        return NAMESPACED_KEY_MAP.get(key);
    }

    @Override
    public void onEnable() {
        plugin = this;

        createFolders();
        registerListener();

        try {
            ImplementationHolder.register();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        ImplementationHolder.dispose();

        NAMESPACED_KEY_MAP.clear();
    }

    private void registerListener() {
        List.of(
                new PlayerJoinListener(this),
                new PlayerLeaveListener(this)
        ).forEach(SpigotEventListener::register);
    }

    private void createFolders() {

    }

    /* Implementation of ImprovedFactions */
    @Override
    public @Nullable FactionPlayer<?> getPlayer(@NotNull UUID id) {
        Player player = Bukkit.getPlayer(id);
        if (player == null) return null;
        return new SpigotFactionPlayer(player, this);
    }

    @Override
    public @Nullable FactionPlayer<?> getPlayer(@NotNull String name) {
        Player player = Bukkit.getPlayer(name);
        if (player == null) return null;
        return new SpigotFactionPlayer(player, this);
    }

    @Override
    public @Nullable OfflineFactionPlayer<?> getOfflinePlayer(@NotNull UUID id) {
    }

    @Override
    public @Nullable OfflineFactionPlayer<?> getOfflinePlayer(@NotNull String name) {
    }

    @Override
    public @Nullable World getWorld(@NotNull String name) {
        org.bukkit.World world = Bukkit.getWorld(name);
        if (world == null) return null;
        return new SpigotWorld(world, this);
    }

    @Override
    public @NotNull List<World> getAllWorlds() {
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
        return return new File(getDataFolder(), "lang");
        ;
    }
}
