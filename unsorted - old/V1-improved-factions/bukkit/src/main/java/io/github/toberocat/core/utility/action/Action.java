package io.github.toberocat.core.utility.action;

import io.github.toberocat.core.factions.Faction;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
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

    public void run(@NotNull Player player) {
        // override
    }

    public void run(@NotNull Player player, @NotNull String[] args) {
        // override
    }

    public void run(@NotNull Player player, @NotNull String provided) {
        // override
    }

    /* Faction */

    public void run(@NotNull Faction player, @NotNull String[] args) {
        // override
    }

    public void run(@NotNull Faction player, @NotNull String provided) {}
}
