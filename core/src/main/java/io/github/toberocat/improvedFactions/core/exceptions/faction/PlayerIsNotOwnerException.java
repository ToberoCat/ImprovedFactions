package io.github.toberocat.improvedFactions.core.exceptions.faction;

import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.sender.player.FactionPlayer;
import org.jetbrains.annotations.NotNull;

public class PlayerIsNotOwnerException extends FactionException {
    public PlayerIsNotOwnerException(@NotNull Faction<?> faction, @NotNull FactionPlayer<?> owner) {
        super(faction, "Player " + owner + " isn't the owner of this faction");
    }
}
