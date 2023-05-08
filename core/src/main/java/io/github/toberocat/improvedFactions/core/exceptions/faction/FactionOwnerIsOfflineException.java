package io.github.toberocat.improvedFactions.core.exceptions.faction;

import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.translator.PlaceholderBuilder;
import org.jetbrains.annotations.NotNull;

public class FactionOwnerIsOfflineException extends FactionException {
    public FactionOwnerIsOfflineException(@NotNull Faction<?> faction) {
        super(faction, "exceptions.faction-owner-offline", () -> new PlaceholderBuilder()
                .placeholder("faction", faction)
                .getPlaceholders());
    }
}
