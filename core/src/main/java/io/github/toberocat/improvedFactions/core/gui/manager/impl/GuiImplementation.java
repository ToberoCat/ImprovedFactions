package io.github.toberocat.improvedFactions.core.gui.manager.impl;

import io.github.toberocat.improvedFactions.core.player.CommandSender;
import io.github.toberocat.improvedFactions.core.player.ConsoleCommandSender;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import org.jetbrains.annotations.NotNull;

public interface GuiImplementation {
    void openEditor(@NotNull CommandSender sender, @NotNull String guiId);
    void openGui(@NotNull FactionPlayer<?> player, @NotNull String guiId);
}
