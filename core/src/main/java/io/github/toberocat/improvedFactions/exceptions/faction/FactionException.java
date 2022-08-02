package io.github.toberocat.improvedFactions.exceptions.faction;

import io.github.toberocat.improvedFactions.faction.Faction;
import org.jetbrains.annotations.NotNull;

public class FactionException extends Exception {
    private final Faction<?> faction;

    public FactionException(@NotNull Faction<?> faction,
                            @NotNull String message,
                            @NotNull Object... placeholders) {
        super(String.format(message, placeholders));
        this.faction = faction;
    }

    public Faction<?> getFaction() {
        return faction;
    }
}
