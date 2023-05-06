package io.github.toberocat.improvedFactions.core.action;

import io.github.toberocat.improvedFactions.core.player.CommandSender;
import io.github.toberocat.improvedFactions.core.player.ConsoleCommandSender;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import org.jetbrains.annotations.NotNull;

public abstract class Action {

    public final void register() {
        ActionCore.register(this);
    }

    /**
     * Gets the text in the brackets at the beginning of the action.
     * Should not have uppercase letters or spaces and excludes the brackets.
     *
     * @return the label for this action.
     */
    public abstract @NotNull String label();

    /* Sender */

    public void run(@NotNull CommandSender commandSender) {
        // override
    }

    public void run(@NotNull CommandSender commandSender, @NotNull String[] args) {
        // override
    }

    public void run(@NotNull CommandSender commandSender, @NotNull String provided) {
        // override
    }

    /* Console */

    public void run(@NotNull ConsoleCommandSender consoleCommandSender) {
        // override
    }

    public void run(@NotNull ConsoleCommandSender consoleCommandSender, @NotNull String[] args) {
        // override
    }

    public void run(@NotNull ConsoleCommandSender consoleCommandSender, @NotNull String provided) {
        // override
    }

    /* Player */

    public void run(@NotNull FactionPlayer player) {
        // override
    }

    public void run(@NotNull FactionPlayer player, @NotNull String[] args) {
        // override
    }

    public void run(@NotNull FactionPlayer player, @NotNull String provided) {
        // override
    }
}
