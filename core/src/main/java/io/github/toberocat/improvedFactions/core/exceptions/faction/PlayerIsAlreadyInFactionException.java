package io.github.toberocat.improvedFactions.core.exceptions.faction;

import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.player.OfflineFactionPlayer;
import org.jetbrains.annotations.NotNull;

public class PlayerIsAlreadyInFactionException extends FactionException {

    private final OfflineFactionPlayer<?> player;

    public PlayerIsAlreadyInFactionException(@NotNull Faction<?> faction, @NotNull OfflineFactionPlayer<?> player) {
        super(faction, String.format("PLayer %s is already in a faction", player.getName()));
        this.player = player;
    }

    public OfflineFactionPlayer<?> getPlayer() {
        return player;
    }
}
