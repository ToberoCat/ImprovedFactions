package io.github.toberocat.improvedfactions.spigot.placeholder;

import io.github.toberocat.improvedFactions.core.placeholder.PlaceholderFormatter;
import io.github.toberocat.improvedFactions.core.player.OfflineFactionPlayer;
import io.github.toberocat.improvedfactions.spigot.MainIF;
import io.github.toberocat.improvedfactions.spigot.player.SpigotOfflineFactionPlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class FactionExpansion extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "faction";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Tobero";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @NotNull String getVersion() {
        return JavaPlugin.getPlugin(MainIF.class).getCurrentVersion();
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        Function<OfflineFactionPlayer<?>, String> function =
                PlaceholderFormatter.getPlaceholders().get(params);
        if (function == null) return null;
        return function.apply(new SpigotOfflineFactionPlayer(player));
    }
}
