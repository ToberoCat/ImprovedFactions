package io.github.toberocat.improvedFactions.core.gui;

import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import org.jetbrains.annotations.NotNull;

public interface GuiImplementation {
    void openEditor(@NotNull FactionPlayer<?> player);
    void openGui(@NotNull FactionPlayer<?> player, @NotNull String guiId);
}
