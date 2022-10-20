package io.github.toberocat.improvedfactions.spigot.gui.provided;

import io.github.toberocat.improvedFactions.core.gui.GuiImplementation;
import io.github.toberocat.improvedFactions.core.gui.GuiManager;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpigotGuiImplementationManager implements GuiImplementation {
    @Override
    public void openEditor(@NotNull FactionPlayer<?> player) {
        new SpigotGuiSelector((Player) player.getRaw());
    }

    @Override
    public void openGui(@NotNull FactionPlayer<?> player,
                        @NotNull String guiId) {
        new SpigotGuiView((Player) player.getRaw(), GuiManager.getGui(guiId));
    }

}
