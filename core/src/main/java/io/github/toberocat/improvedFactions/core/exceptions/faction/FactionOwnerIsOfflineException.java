package io.github.toberocat.improvedFactions.core.exceptions.faction;

import io.github.toberocat.improvedFactions.core.faction.Faction;
import org.jetbrains.annotations.NotNull;

public class FactionOwnerIsOfflineException extends Exception {

    private final Faction<?> faction;

    public FactionOwnerIsOfflineException(@NotNull Faction<?> faction) {
        super("Owner of " + faction.getRegistry() + " is offline");
        this.faction = faction;
    }

    public Faction<?> getFaction() {
        return faction;
    }
}
