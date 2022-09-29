package io.github.toberocat.improvedfactions.spigot.gui.provided;

import io.github.toberocat.improvedFactions.core.gui.EditorGui;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpigotEditorGuiManager implements EditorGui {
    @Override
    public void open(@NotNull FactionPlayer<?> player) {
        Player user = Bukkit.getPlayer(player.getUniqueId());
        if (user == null) return;

        new SpigotGuiSelector(user);
    }
}
