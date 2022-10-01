/**
 * Created: 01/10/2022
 *
 * @author Tobias Madlberger (Tobias)
 */

package io.github.toberocat.improvedfactions.spigot.placeholder.provided;

import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.utils.Formatting;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class MaxPowerPlaceholder implements FactionPlaceholder {
    @Override
    public @NotNull String run(@NotNull OfflinePlayer player, @NotNull Faction<?> faction) {
        return Formatting.shorten(faction.getTotalMaxPower());
    }
}
