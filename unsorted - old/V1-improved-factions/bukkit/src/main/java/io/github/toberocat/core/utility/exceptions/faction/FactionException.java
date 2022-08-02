package io.github.toberocat.core.utility.exceptions.faction;

import io.github.toberocat.core.factions.Faction;
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
