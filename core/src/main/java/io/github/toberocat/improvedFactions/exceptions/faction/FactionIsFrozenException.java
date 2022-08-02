package io.github.toberocat.improvedFactions.exceptions.faction;

import io.github.toberocat.improvedFactions.faction.Faction;
import org.jetbrains.annotations.NotNull;

public class FactionIsFrozenException extends FactionException {
    public FactionIsFrozenException(@NotNull Faction<?> faction) {
        super(faction, "Faction %s is frozen", faction.getRegistry());
    }
}
