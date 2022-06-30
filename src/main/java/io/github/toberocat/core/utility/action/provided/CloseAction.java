package io.github.toberocat.core.utility.action.provided;

import io.github.toberocat.core.utility.action.Action;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CloseAction extends Action {

    @Override
    public @NotNull String label() {
        return "close";
    }

    @Override
    public void run(@NotNull Player player) {
        player.closeInventory();
    }
}
