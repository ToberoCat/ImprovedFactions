package io.github.toberocat.improvedFactions.core.placeholder.provided;

import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.core.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.player.OfflineFactionPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;


public interface FactionPlaceholder extends Function<OfflineFactionPlayer<?>, String> {
    @NotNull String run(@NotNull OfflineFactionPlayer<?> player, @NotNull Faction<?> faction);

    @Override
    default String apply(OfflineFactionPlayer<?> player) {
        try {
            return run(player, player.getFaction());
        } catch (PlayerHasNoFactionException | FactionNotInStorage e) {
            return null;
        }
    }
}
