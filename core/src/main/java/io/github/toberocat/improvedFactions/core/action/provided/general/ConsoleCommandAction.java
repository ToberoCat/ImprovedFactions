package io.github.toberocat.improvedFactions.core.action.provided.general;

import io.github.toberocat.improvedFactions.core.action.Action;
import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.player.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ConsoleCommandAction extends Action {

    @Override
    public @NotNull String label() {
        return "console";
    }

    @Override
    public void run(@NotNull CommandSender commandSender, @NotNull String provided) {
        ImprovedFactions.api().runCommand(provided);
    }
}
