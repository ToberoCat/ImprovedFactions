package io.github.toberocat.improvedFactions.exceptions.faction;

import org.jetbrains.annotations.NotNull;

public class PlayerIsOwnerException extends Exception {
    public PlayerIsOwnerException(@NotNull String player) {
        super("Player " + player + " is owner of this faction and therefore can't leave");
    }
}
