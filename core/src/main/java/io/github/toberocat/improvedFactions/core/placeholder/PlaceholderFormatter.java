/**
 * Created: 01/10/2022
 *
 * @author Tobias Madlberger (Tobias)
 */

package io.github.toberocat.improvedFactions.core.placeholder;

import io.github.toberocat.improvedFactions.core.placeholder.provided.CurrentPowerPlaceholder;
import io.github.toberocat.improvedFactions.core.placeholder.provided.MaxPowerPlaceholder;
import io.github.toberocat.improvedFactions.core.placeholder.provided.MotdPlaceholder;
import io.github.toberocat.improvedFactions.core.placeholder.provided.RankPlaceholder;
import io.github.toberocat.improvedFactions.core.player.OfflineFactionPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class PlaceholderFormatter {
    private static final Map<String, Function<OfflineFactionPlayer<?>, String>> placeholders =
            new HashMap<>();

    static {
        register("currentpower", new CurrentPowerPlaceholder());
        register("maxpower", new MaxPowerPlaceholder());
        register("rank", new RankPlaceholder());
        register("motd", new MotdPlaceholder());
    }

    public static void register(@NotNull String placeholder,
                                @NotNull Function<OfflineFactionPlayer<?>, String> query) {
        placeholders.put(placeholder, query);
    }

    public static Map<String, Function<OfflineFactionPlayer<?>, String>> getPlaceholders() {
        return placeholders;
    }

    public static @NotNull String parse(OfflineFactionPlayer<?> player, @NotNull String text) {
        var ref = new Object() {
            String fText = text;
        };
        placeholders.forEach((literal, function) ->
                ref.fText = ref.fText.replaceAll("%faction_" + literal + "%",
                        function.apply(player)));

        return ref.fText;
    }
}
