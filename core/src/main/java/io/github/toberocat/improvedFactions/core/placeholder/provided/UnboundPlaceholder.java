package io.github.toberocat.improvedFactions.core.placeholder.provided;

import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.core.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.player.OfflineFactionPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface UnboundPlaceholder {
    boolean canParse(@NotNull String placeholder);

    @NotNull String run(@NotNull String placeholder,
                        @NotNull OfflineFactionPlayer<?> player,
                        Faction<?> faction);

    default String apply(@NotNull String placeholder, @NotNull  OfflineFactionPlayer<?> player) {
        try {
            return run(placeholder, player, player.getFaction());
        } catch (PlayerHasNoFactionException | FactionNotInStorage e) {
            return null;
        }
    }
}
