package io.github.toberocat.improvedFactions.core.gui;

import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import org.jetbrains.annotations.NotNull;

public interface EditorGui {
    void open(@NotNull FactionPlayer<?> player);
}
