package io.github.toberocat.improvedFactions.core.exceptions.faction;

import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.translator.PlaceholderBuilder;
import org.jetbrains.annotations.NotNull;


public class FactionCantBeRenamedToThisLiteralException extends FactionException {
    public FactionCantBeRenamedToThisLiteralException(@NotNull Faction<?> faction, @NotNull String text) {
        super(faction, "exceptions.faction-cant-be-renamed", new PlaceholderBuilder()
                .placeholder("faction", faction)
                .placeholder("text", text)
                .getPlaceholders());
    }
}
