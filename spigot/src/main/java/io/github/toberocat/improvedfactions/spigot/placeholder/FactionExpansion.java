/**
 * Created: 01/10/2022
 *
 * @author Tobias Madlberger (Tobias)
 */

package io.github.toberocat.improvedfactions.spigot.placeholder;

import io.github.toberocat.improvedfactions.spigot.MainIF;
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
        Function<OfflinePlayer, String> function = PlaceholderFormatter.getPlaceholders().get(params);
        if (function == null) return null;
        return function.apply(player);
    }
}
