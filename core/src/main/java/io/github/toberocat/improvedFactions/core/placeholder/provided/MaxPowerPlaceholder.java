package io.github.toberocat.improvedFactions.core.placeholder.provided;

import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.player.OfflineFactionPlayer;
import io.github.toberocat.improvedFactions.core.utils.Formatting;
import org.jetbrains.annotations.NotNull;

public class MaxPowerPlaceholder implements FactionPlaceholder {
    @Override
    public @NotNull String run(@NotNull Faction<?> faction) {
        return Formatting.shorten(faction.getTotalMaxPower());
    }

    @Override
    public @NotNull String label() {
        return "maxpower";
    }
}
