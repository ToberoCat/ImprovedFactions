/**
 * Created: 01/10/2022
 *
 * @author Tobias Madlberger (Tobias)
 */

package io.github.toberocat.improvedFactions.core.placeholder;

import io.github.toberocat.improvedFactions.core.placeholder.provided.*;
import io.github.toberocat.improvedFactions.core.player.OfflineFactionPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
public class PlaceholderFormatter {
    private static final List<UnboundPlaceholder> PLACEHOLDERS = new ArrayList<>();

    static {
        register(new RegistryPlaceholder());
        register(new DisplayPlaceholder());
        register(new CurrentPowerPlaceholder());
        register(new MaxPowerPlaceholder());
        register(new RankPlaceholder());
        register(new MotdPlaceholder());
        register(new ChunksPlaceholder());
    }

    public static void register(@NotNull UnboundPlaceholder query) {
        PLACEHOLDERS.add(query);
    }

    public static @Nullable UnboundPlaceholder getPlaceholder(@NotNull String identifier) {
        return PLACEHOLDERS.stream()
                .filter(x -> x.canParse(identifier))
                .findAny()
                .orElse(null);
    }

    public static @NotNull String parse(OfflineFactionPlayer player, @NotNull String text) {
        String[] parts = text.split("%");
        for (int i = 0; i < parts.length; i += 2) {
            if (i + 1 < parts.length) {
                String placeholder = parts[i + 1];
                UnboundPlaceholder action = PLACEHOLDERS
                        .stream()
                        .filter(x -> x.canParse(placeholder))
                        .findFirst()
                        .orElse(null);

                if (action != null)
                    parts[i + 1] = action.apply(player, placeholder);
            }
        }

        return String.join(" ", parts);
    }
}
