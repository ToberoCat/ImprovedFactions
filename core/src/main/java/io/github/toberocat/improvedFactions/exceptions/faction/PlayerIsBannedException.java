package io.github.toberocat.improvedFactions.exceptions.faction;

import io.github.toberocat.improvedFactions.faction.Faction;
import io.github.toberocat.improvedFactions.player.OfflineFactionPlayer;
import org.jetbrains.annotations.NotNull;

public class PlayerIsBannedException extends FactionException {

    private final OfflineFactionPlayer<?> player;

    public PlayerIsBannedException(@NotNull Faction<?> faction, @NotNull OfflineFactionPlayer<?> player) {
        super(faction, String.format("Player %s is banned in faction %s", player.getName(), faction.getRegistry()));
        this.player = player;
    }

    public OfflineFactionPlayer<?> getPlayer() {
        return player;
    }

}
