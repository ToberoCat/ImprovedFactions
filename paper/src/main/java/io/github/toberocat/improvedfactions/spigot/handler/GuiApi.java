package io.github.toberocat.improvedfactions.spigot.handler;

import io.github.toberocat.guiengine.api.GuiEngineApi;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GuiApi implements io.github.toberocat.improvedFactions.core.gui.GuiApi {
    private final GuiEngineApi api;

    public GuiApi(@NotNull GuiEngineApi api) {
        this.api = api;
    }

    @Override
    public void openGui(@NotNull FactionPlayer player, @NotNull String guiId) {
        if (!(player.getRaw() instanceof Player bukkitPlayer))
            return;
        api.openGui(bukkitPlayer, guiId);
    }
}
