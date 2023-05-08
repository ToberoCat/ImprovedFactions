package io.github.toberocat.improvedFactions.core.exceptions.faction;

import io.github.toberocat.improvedFactions.core.exceptions.TranslatableException;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.player.OfflineFactionPlayer;
import io.github.toberocat.improvedFactions.core.translator.PlaceholderBuilder;
import org.jetbrains.annotations.NotNull;


public class PlayerNotAMember extends TranslatableException {
    public PlayerNotAMember(@NotNull OfflineFactionPlayer player, @NotNull Faction<?> faction) {
        super("exceptions.player-not-a-member", () -> new PlaceholderBuilder()
                .placeholder("player", player)
                .placeholder("faction", faction)
                .getPlaceholders());
    }
}
