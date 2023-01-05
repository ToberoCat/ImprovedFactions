package io.github.toberocat.improvedfactions.spigot.placeholder;

import io.github.toberocat.improvedFactions.core.placeholder.PlaceholderFormatter;
import io.github.toberocat.improvedFactions.core.placeholder.provided.UnboundPlaceholder;
import io.github.toberocat.improvedfactions.spigot.MainIF;
import io.github.toberocat.improvedfactions.spigot.player.SpigotOfflineFactionPlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


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
        UnboundPlaceholder placeholder =
                PlaceholderFormatter.getPlaceholder(params);
        if (placeholder == null) return null;
        return placeholder.apply(new SpigotOfflineFactionPlayer(player), params);
    }
}
