package io.github.toberocat.improvedFactions.core.exceptions.faction;

import io.github.toberocat.improvedFactions.core.faction.Faction;
import org.jetbrains.annotations.NotNull;

public class FactionAlreadyExistsException extends FactionException {
    public FactionAlreadyExistsException(@NotNull Faction<?> faction) {
        super(faction, "A faction already exists with this registry name");
    }
}
