package io.github.toberocat.improvedFactions.core.exceptions.faction;

import io.github.toberocat.improvedFactions.core.exceptions.TranslatableException;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class FactionException extends TranslatableException {

    protected final Faction<?> faction;

    public FactionException(@NotNull Faction<?> faction,
                            @NotNull String translationKey,
                            @NotNull Map<String, String> placeholders) {
        super(translationKey, placeholders);
        this.faction = faction;
    }


    public Faction<?> getFaction() {
        return faction;
    }
}
