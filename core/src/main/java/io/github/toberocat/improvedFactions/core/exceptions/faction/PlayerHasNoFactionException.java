package io.github.toberocat.improvedFactions.core.exceptions.faction;

import io.github.toberocat.improvedFactions.core.exceptions.TranslatableException;
import io.github.toberocat.improvedFactions.core.player.OfflineFactionPlayer;
import io.github.toberocat.improvedFactions.core.translator.PlaceholderBuilder;
import org.jetbrains.annotations.NotNull;

public class PlayerHasNoFactionException extends TranslatableException {
    public PlayerHasNoFactionException(@NotNull OfflineFactionPlayer player) {
        super("exceptions.player-has-no-faction", new PlaceholderBuilder()
                .placeholder("player", player)
                .getPlaceholders());
    }
}
