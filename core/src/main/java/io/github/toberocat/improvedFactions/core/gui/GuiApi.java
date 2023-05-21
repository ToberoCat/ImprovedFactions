package io.github.toberocat.improvedFactions.core.gui;

import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface GuiApi {
    void openGui(@NotNull FactionPlayer player, @NotNull String guiId);

    void openGui(@NotNull FactionPlayer player,
                 @NotNull String guiId,
                 @NotNull Map<String, String> placeholders);

    void openSettingsGui(@NotNull FactionPlayer player);
}
