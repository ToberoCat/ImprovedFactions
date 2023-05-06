package io.github.toberocat.improvedFactions.core.exceptions.faction;

import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.player.OfflineFactionPlayer;
import io.github.toberocat.improvedFactions.core.translator.PlaceholderBuilder;
import org.jetbrains.annotations.NotNull;

public class PlayerIsBannedException extends FactionException {

    private final OfflineFactionPlayer player;

    public PlayerIsBannedException(@NotNull Faction<?> faction, @NotNull OfflineFactionPlayer player) {
        super(faction, "exceptions.player-is-banned", new PlaceholderBuilder()
                .placeholder("faction", faction)
                .getPlaceholders());
        this.player = player;
    }

    public OfflineFactionPlayer getPlayer() {
        return player;
    }

}
