package io.github.toberocat.improvedFactions.core.placeholder.provided;

import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.core.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.player.OfflineFactionPlayer;
import org.jetbrains.annotations.NotNull;

public interface PlayerPlaceholder extends UnboundPlaceholder {

    @NotNull String label();

    @NotNull String run(@NotNull OfflineFactionPlayer player, @NotNull Faction<?> faction);
    @Override
    default boolean canParse(@NotNull String placeholder) {
        return placeholder.equals(label());
    }

    @Override
    @NotNull
    default String apply(@NotNull OfflineFactionPlayer player,
                         @NotNull String placeholder) {
        try {
            return run(player, player.getFaction());
        } catch (PlayerHasNoFactionException | FactionNotInStorage e) {
            return "";
        }
    }
}
