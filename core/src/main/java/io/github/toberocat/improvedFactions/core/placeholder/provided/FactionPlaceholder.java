package io.github.toberocat.improvedFactions.core.placeholder.provided;

import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.core.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.faction.handler.FactionHandler;
import io.github.toberocat.improvedFactions.core.placeholder.PlaceholderFormatter;
import io.github.toberocat.improvedFactions.core.player.OfflineFactionPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;


public interface FactionPlaceholder extends UnboundPlaceholder {
    @NotNull String run(@NotNull Faction<?> faction);

    @NotNull String label();

    @Override
    default boolean canParse(@NotNull String placeholder) {
        return placeholder.contains(label());
    }

    @Override
    @NotNull
    default String apply(@NotNull OfflineFactionPlayer player,
                         @NotNull String placeholder) {
        try {
            return run(player.getFaction());
        } catch (PlayerHasNoFactionException | FactionNotInStorage e) {
            String registry = placeholder.replace("_" + label(), "");
            try {
                return run(FactionHandler.getFaction(registry));
            } catch (FactionNotInStorage ex) {
                return "";
            }
        }
    }
}
