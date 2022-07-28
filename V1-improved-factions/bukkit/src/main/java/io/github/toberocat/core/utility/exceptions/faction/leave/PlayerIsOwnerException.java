package io.github.toberocat.core.utility.exceptions.faction.leave;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.utility.exceptions.faction.FactionException;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class PlayerIsOwnerException extends FactionException {
    public PlayerIsOwnerException(@NotNull Faction<?> faction, @NotNull OfflinePlayer player) {
        super(faction, "Player " + player.getName() + " is owner of " + faction + " and can't leave it. PLease delete it, " +
                "transfer the ownership or make it permanent");
    }
}

