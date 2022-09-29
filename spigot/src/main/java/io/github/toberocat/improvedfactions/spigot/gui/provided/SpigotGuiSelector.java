package io.github.toberocat.improvedfactions.spigot.gui.provided;

import io.github.toberocat.improvedFactions.core.gui.GuiManager;
import io.github.toberocat.improvedFactions.core.gui.JsonGui;
import io.github.toberocat.improvedfactions.spigot.gui.TabbedGui;
import io.github.toberocat.improvedfactions.spigot.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class SpigotGuiSelector extends TabbedGui {
    public SpigotGuiSelector(@NotNull Player player) {
        super(player, createInventory(player, 54, "Select a gui"));
        GuiManager.getGuis().forEach(x -> {
            addSlot(ItemUtils.createItem(Material.BOOK, x), u -> {
                try {
                    JsonGui gui = GuiManager.getGui(x);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });
    }
}
