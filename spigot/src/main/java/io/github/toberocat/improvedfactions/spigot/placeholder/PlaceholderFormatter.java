/**
 * Created: 01/10/2022
 *
 * @author Tobias Madlberger (Tobias)
 */

package io.github.toberocat.improvedfactions.spigot.placeholder;

import io.github.toberocat.improvedfactions.spigot.placeholder.provided.CurrentPowerPlaceholder;
import io.github.toberocat.improvedfactions.spigot.placeholder.provided.MaxPowerPlaceholder;
import io.github.toberocat.improvedfactions.spigot.placeholder.provided.RankPlaceholder;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class PlaceholderFormatter {
    private static final Map<String, Function<OfflinePlayer, String>> placeholders = new HashMap<>();

    public static void register(@NotNull String placeholder, @NotNull Function<OfflinePlayer, String> query) {
        placeholders.put(placeholder, query);
    }

    public static Map<String, Function<OfflinePlayer, String>> getPlaceholders() {
        return placeholders;
    }

    static {
        register("currentpower", new CurrentPowerPlaceholder());
        register("maxpower", new MaxPowerPlaceholder());
        register("rank", new RankPlaceholder());
    }

    public static @NotNull String parse(OfflinePlayer player, @NotNull String text) {
        var ref = new Object() {
            String fText = text;
        };
        placeholders.forEach((literal, function) -> {
            ref.fText = ref.fText.replaceAll("%faction_" + literal + "%", function.apply(player));
        });

        return ref.fText;
    }
}
