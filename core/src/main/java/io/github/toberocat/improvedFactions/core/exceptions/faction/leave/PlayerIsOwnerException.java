package io.github.toberocat.improvedFactions.core.exceptions.faction.leave;

import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionException;
import io.github.toberocat.improvedFactions.core.sender.player.OfflineFactionPlayer;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import org.jetbrains.annotations.NotNull;

public class PlayerIsOwnerException extends FactionException {
    public PlayerIsOwnerException(@NotNull Faction<?> faction, @NotNull OfflineFactionPlayer<?> player) {
        super(faction, "Player " + player.getName() + " is owner of " + faction + " and can't leave it. PLease delete it, " +
                "transfer the ownership or make it permanent");
    }
}

