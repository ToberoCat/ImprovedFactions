package io.github.toberocat.improvedfactions.spigot.gui.provided;

import io.github.toberocat.improvedFactions.core.gui.manager.impl.BrowserGuiImplementation;
import io.github.toberocat.improvedFactions.core.gui.manager.GuiManager;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpigotGuiImplementationManager implements BrowserGuiImplementation {
    @Override
    public void openGui(@NotNull FactionPlayer<?> player,
                        @NotNull String guiId) {
        new SpigotGuiView((Player) player.getRaw(), GuiManager.getGui(guiId));
    }

}
