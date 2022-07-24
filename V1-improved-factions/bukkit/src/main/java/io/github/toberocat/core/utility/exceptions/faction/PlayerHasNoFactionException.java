package io.github.toberocat.core.utility.exceptions.faction;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlayerHasNoFactionException extends Exception {
    public PlayerHasNoFactionException(@NotNull Player player) {
        super("Player " + player.getName() + " is in no faction");
    }
}
