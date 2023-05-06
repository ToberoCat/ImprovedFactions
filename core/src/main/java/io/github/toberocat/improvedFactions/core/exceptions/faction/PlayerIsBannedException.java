package io.github.toberocat.improvedFactions.core.exceptions.faction;

import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.player.OfflineFactionPlayer;
import org.jetbrains.annotations.NotNull;

public class PlayerIsBannedException extends FactionException {

    private final OfflineFactionPlayer player;

    public PlayerIsBannedException(@NotNull Faction<?> faction, @NotNull OfflineFactionPlayer player) {
        super(faction, "exceptions.player-is-banned", new BuildableMap<String, String>()
                .set("faction"));
        this.player = player;
    }

    public OfflineFactionPlayer getPlayer() {
        return player;
    }

}
