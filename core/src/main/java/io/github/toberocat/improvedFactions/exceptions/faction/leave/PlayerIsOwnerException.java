package io.github.toberocat.improvedFactions.exceptions.faction.leave;

import io.github.toberocat.improvedFactions.exceptions.faction.FactionException;
import io.github.toberocat.improvedFactions.faction.Faction;
import io.github.toberocat.improvedFactions.player.OfflineFactionPlayer;
import org.jetbrains.annotations.NotNull;

public class PlayerIsOwnerException extends FactionException {
    public PlayerIsOwnerException(@NotNull Faction<?> faction, @NotNull OfflineFactionPlayer<?> player) {
        super(faction, "Player " + player.getName() + " is owner of " + faction + " and can't leave it. PLease delete it, " +
                "transfer the ownership or make it permanent");
    }
}

