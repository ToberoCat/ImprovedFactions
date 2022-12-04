package io.github.toberocat.improvedFactions.core.action.provided.general;

import io.github.toberocat.improvedFactions.core.action.Action;
import io.github.toberocat.improvedFactions.core.gui.manager.GuiManager;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import org.jetbrains.annotations.NotNull;

public class OpenMenuAction extends Action {

    @Override
    public @NotNull String label() {
        return "open-gui";
    }

    @Override
    public void run(@NotNull FactionPlayer<?> player, @NotNull String provided) {
        GuiManager.openGui(provided, player);
    }
}
