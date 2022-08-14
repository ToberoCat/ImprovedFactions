package io.github.toberocat.improvedfactions;

import io.github.toberocat.improvedFactions.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.player.FactionPlayer;
import io.github.toberocat.improvedFactions.player.OfflineFactionPlayer;
import io.github.toberocat.improvedFactions.registry.ImplementationHolder;
import io.github.toberocat.improvedFactions.world.World;
import io.github.toberocat.improvedfactions.player.SpigotFactionPlayer;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class MainIF extends JavaPlugin implements ImprovedFactions {

    private final Map<String, NamespacedKey> NAMESPACED_KEY_MAP = new LinkedHashMap<>();
    private MainIF plugin;

    public @NotNull NamespacedKey getNamespacedKey(@NotNull String key) {
        if (!NAMESPACED_KEY_MAP.containsKey(key))
            NAMESPACED_KEY_MAP.put(key, new NamespacedKey(plugin, key));

        return NAMESPACED_KEY_MAP.get(key);
    }

    @Override
    public void onEnable() {
        plugin = this;
        ImplementationHolder.register();
    }

    @Override
    public void onDisable() {
        ImplementationHolder.dispose();

        NAMESPACED_KEY_MAP.clear();
    }

    @Override
    public @Nullable FactionPlayer<?> getPlayer(@NotNull UUID id) {
        Player player = Bukkit.getPlayer(id);
        if (player == null) return null;
        return new SpigotFactionPlayer(player, this);
    }

    @Override
    public @Nullable FactionPlayer<?> getPlayer(@NotNull String name) {
        return null;
    }

    @Override
    public @Nullable OfflineFactionPlayer<?> getOfflinePlayer(@NotNull UUID id) {
        return null;
    }

    @Override
    public @Nullable OfflineFactionPlayer<?> getOfflinePlayer(@NotNull String name) {
        return null;
    }

    @Override
    public @Nullable World getWorld(@NotNull String name) {
        return null;
    }

    @Override
    public @NotNull List<World> getAllWorlds() {
        return null;
    }

    public MainIF getPlugin() {
        return plugin;
    }
}
