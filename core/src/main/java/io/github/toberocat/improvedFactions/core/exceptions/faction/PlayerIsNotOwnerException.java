package io.github.toberocat.improvedFactions.core.exceptions.faction;

import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import org.jetbrains.annotations.NotNull;

public class PlayerIsNotOwnerException extends FactionException {
    public PlayerIsNotOwnerException(@NotNull Faction<?> faction, @NotNull FactionPlayer owner) {
        super(faction, "exceptions.player-not-owner", new BuildableMap<String, String>()
                .set("owner", owner.getName()));
    }
}
