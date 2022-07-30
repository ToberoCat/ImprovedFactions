package io.github.toberocat.core.utility.exceptions.faction;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class PlayerHasNoFactionException extends Exception {
    public PlayerHasNoFactionException(@NotNull OfflinePlayer player) {
        super("Player " + player.getName() + " is in no faction");
    }
}
