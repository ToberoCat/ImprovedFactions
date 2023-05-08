package io.github.toberocat.improvedFactions.core.exceptions.faction;

import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.translator.PlaceholderBuilder;
import io.github.toberocat.improvedFactions.core.world.Chunk;
import org.jetbrains.annotations.NotNull;

public class FactionDoesntOwnChunkException extends FactionException {

    public FactionDoesntOwnChunkException(@NotNull Faction<?> faction,
                                          @NotNull Chunk chunk) {
        super(faction, "exceptions.faction-doesnt-own-chunk", () -> new PlaceholderBuilder()
                .placeholder("faction", faction)
                .placeholder("chunk", chunk)
                .getPlaceholders());
    }
}
