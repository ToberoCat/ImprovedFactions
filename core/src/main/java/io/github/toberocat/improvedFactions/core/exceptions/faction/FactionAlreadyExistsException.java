package io.github.toberocat.improvedFactions.core.exceptions.faction;

import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.translator.PlaceholderBuilder;
import org.jetbrains.annotations.NotNull;

public class FactionAlreadyExistsException extends FactionException {
    public FactionAlreadyExistsException(@NotNull Faction<?> faction) {
        super(faction, "exceptions.faction-already-exists", new PlaceholderBuilder()
                .placeholder("faction", faction)
                .getPlaceholders());
    }
}
