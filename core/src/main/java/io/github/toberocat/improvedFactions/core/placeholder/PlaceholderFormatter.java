/**
 * Created: 01/10/2022
 *
 * @author Tobias Madlberger (Tobias)
 */

package io.github.toberocat.improvedFactions.core.placeholder;

import io.github.toberocat.improvedFactions.core.placeholder.provided.*;
import io.github.toberocat.improvedFactions.core.player.OfflineFactionPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class PlaceholderFormatter {
    private static final Map<String, Function<OfflineFactionPlayer<?>, String>> placeholders =
            new HashMap<>();
    private static final List<UnboundPlaceholder> universalPlaceholders = new ArrayList<>();

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

    public static void register(@NotNull UnboundPlaceholder query) {
        universalPlaceholders.add(query);
    }


    public static Map<String, Function<OfflineFactionPlayer<?>, String>> getPlaceholders() {
        return placeholders;
    }

    public static @NotNull String parse(OfflineFactionPlayer<?> player, @NotNull String text) {
        String[] parts = text.split("%");
        for (int i = 0; i < parts.length; i += 2) {
            System.out.println("Text: " + parts[i]);
            if (i + 1 < parts.length) {
                String placeholder = parts[i + 1];
                if (!placeholders.containsKey(placeholder)) {
                    UnboundPlaceholder action = universalPlaceholders
                            .stream()
                            .filter(x -> x.canParse(placeholder))
                            .findFirst()
                            .orElse(null);

                    if (action != null)
                        parts[i + 1] = action.apply(placeholder, player);
                } else parts[i + 1] = placeholders.get(placeholder).apply(player);
            }
        }

        return String.join(" ", parts);
    }
}
