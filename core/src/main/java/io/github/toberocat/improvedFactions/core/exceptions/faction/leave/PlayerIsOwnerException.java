package io.github.toberocat.improvedFactions.core.exceptions.faction.leave;

import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionException;
import io.github.toberocat.improvedFactions.core.player.OfflineFactionPlayer;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.translator.PlaceholderBuilder;
import org.jetbrains.annotations.NotNull;

public class PlayerIsOwnerException extends FactionException {
    public PlayerIsOwnerException(@NotNull Faction<?> faction, @NotNull OfflineFactionPlayer player) {
        super(faction, "exceptions.player-is-owner", () -> new PlaceholderBuilder()
                .placeholder("faction", faction)
                .placeholder("player", player)
                .getPlaceholders());
    }
}

