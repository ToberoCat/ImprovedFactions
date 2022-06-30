package io.github.toberocat.core.utility.action.provided;


import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.action.Action;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class BroadcastAction extends Action {

    @Override
    public @NotNull String label() {
        return "broadcast";
    }

    @Override
    public void run(@NotNull CommandSender commandSender, @NotNull String provided) {
        Bukkit.broadcastMessage(Language.format(provided));
    }
}
