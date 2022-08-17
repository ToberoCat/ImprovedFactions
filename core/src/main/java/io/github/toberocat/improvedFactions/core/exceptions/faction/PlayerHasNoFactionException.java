package io.github.toberocat.improvedFactions.core.exceptions.faction;

import io.github.toberocat.improvedFactions.core.sender.player.OfflineFactionPlayer;
import org.jetbrains.annotations.NotNull;

public class PlayerHasNoFactionException extends Exception {
    public PlayerHasNoFactionException(@NotNull OfflineFactionPlayer<?> player) {
        super("Player " + player.getName() + " is in no faction");
    }
}
