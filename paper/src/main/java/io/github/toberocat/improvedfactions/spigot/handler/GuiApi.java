package io.github.toberocat.improvedfactions.spigot.handler;

import io.github.toberocat.guiengine.api.GuiEngineApi;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class GuiApi implements io.github.toberocat.improvedFactions.core.gui.GuiApi {
    private final GuiEngineApi api;

    public GuiApi(@NotNull JavaPlugin plugin) {
        File file = new File(plugin.getDataFolder(), "guis");
        System.out.println(file);
        this.api = new GuiEngineApi("factions", file);
        this.api.reload();
    }

    @Override
    public void openGui(@NotNull FactionPlayer player, @NotNull String guiId) {
        if (!(player.getRaw() instanceof Player bukkitPlayer))
            return;
        api.openGui(bukkitPlayer, guiId);
    }
}
