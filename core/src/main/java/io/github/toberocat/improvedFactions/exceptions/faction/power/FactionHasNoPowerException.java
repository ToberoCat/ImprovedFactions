package io.github.toberocat.improvedFactions.exceptions.faction.power;

import io.github.toberocat.improvedFactions.exceptions.faction.FactionException;
import io.github.toberocat.improvedFactions.faction.Faction;
import org.jetbrains.annotations.NotNull;

public class FactionHasNoPowerException extends FactionException {
    public FactionHasNoPowerException(@NotNull Faction<?> faction) {
        super(faction, "The faction " + faction.getRegistry() + " has no power left anymore");
    }
}
