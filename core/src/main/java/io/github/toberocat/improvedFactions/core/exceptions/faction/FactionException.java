package io.github.toberocat.improvedFactions.core.exceptions.faction;

import io.github.toberocat.improvedFactions.core.exceptions.TranslatableException;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.translator.Translatable;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Function;

public class FactionException extends TranslatableException {

    protected final @NotNull Faction<?> faction;


    public FactionException(@NotNull Faction<?> faction,
                            @NotNull String translationKey,
                            @NotNull Map<String, Function<Translatable, String>> placeholders) {
        super(translationKey, placeholders);
        this.faction = faction;
    }

    public @NotNull Faction<?> getFaction() {
        return faction;
    }
}
