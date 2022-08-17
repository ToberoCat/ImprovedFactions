package io.github.toberocat.improvedFactions.core.exceptions.faction;

import io.github.toberocat.improvedFactions.core.faction.Faction;
import org.jetbrains.annotations.NotNull;

public class FactionException extends Exception {

    protected final Faction<?> faction;

    public FactionException(@NotNull Faction<?> faction, @NotNull String msg) {
        super(msg);
        this.faction = faction;
    }

    public Faction<?> getFaction() {
        return faction;
    }
}
