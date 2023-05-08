package io.github.toberocat.improvedFactions.core.exceptions.faction.power;

import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionException;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.translator.PlaceholderBuilder;
import org.jetbrains.annotations.NotNull;

public class FactionHasNoPowerException extends FactionException {
    public FactionHasNoPowerException(@NotNull Faction<?> faction) {
        super(faction, "exceptions.faction-has-no-power", () -> new PlaceholderBuilder()
                .placeholder("faction", faction)
                .getPlaceholders());
    }
}
