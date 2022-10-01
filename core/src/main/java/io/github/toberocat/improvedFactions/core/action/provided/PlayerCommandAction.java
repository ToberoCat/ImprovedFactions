package io.github.toberocat.improvedFactions.core.action.provided;

import io.github.toberocat.improvedFactions.core.action.Action;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import org.jetbrains.annotations.NotNull;

public class PlayerCommandAction extends Action {

    @Override
    public @NotNull String label() {
        return "player";
    }

    @Override
    public void run(@NotNull FactionPlayer<?> player, @NotNull String provided) {
        player.runCommand(provided);
    }
}
