package io.github.toberocat.improvedFactions.core.placeholder.provided;

import io.github.toberocat.improvedFactions.core.faction.Faction;
import org.jetbrains.annotations.NotNull;

public class DisplayPlaceholder implements FactionPlaceholder {
    @Override
    public @NotNull String run(@NotNull Faction<?> faction) {
        return faction.getDisplay();
    }

    @Override
    public @NotNull String label() {
        return "getDisplayName";
    }
}
