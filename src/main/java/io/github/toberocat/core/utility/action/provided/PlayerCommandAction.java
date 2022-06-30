package io.github.toberocat.core.utility.action.provided;

import io.github.toberocat.core.utility.action.Action;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class PlayerCommandAction extends Action {

    @Override
    public @NotNull String label() {
        return "player";
    }

    @Override
    public void run(@NotNull CommandSender commandSender, @NotNull String provided) {
        Bukkit.dispatchCommand(commandSender, provided);
    }
}
