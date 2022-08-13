package io.github.toberocat.improvedFactions.exceptions.faction;

import io.github.toberocat.improvedFactions.faction.Faction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IllegalFactionNamingException extends FactionException {

    private final String name;

    public IllegalFactionNamingException(@NotNull Faction<?> faction, @Nullable String name) {
        super(faction, "Faction name " + name + " is not valid");
        this.name = name;
    }

    public @Nullable String getName() {
        return name;
    }
}
