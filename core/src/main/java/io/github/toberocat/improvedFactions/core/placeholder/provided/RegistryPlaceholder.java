package io.github.toberocat.improvedFactions.core.placeholder.provided;

import io.github.toberocat.improvedFactions.core.faction.Faction;
import org.jetbrains.annotations.NotNull;

public class RegistryPlaceholder implements FactionPlaceholder {
    @Override
    public @NotNull String run(@NotNull Faction<?> faction) {
        return faction.getRegistry();
    }

    @Override
    public @NotNull String label() {
        return "registry";
    }
}
