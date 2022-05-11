package io.github.toberocat.core.gui.player;

import io.github.toberocat.core.utility.gui.GUISettings;
import io.github.toberocat.core.utility.gui.Gui;
import io.github.toberocat.core.utility.gui.page.Page;
import io.github.toberocat.core.utility.settings.PlayerSettings;
import io.github.toberocat.core.utility.settings.type.Setting;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class PlayerSettingsGui extends Gui {

    public PlayerSettingsGui(Player player) {
        super(player, createInventory(player), new GUISettings());
        updateGui(player);
    }

    private static Inventory createInventory(Player player) {
        return Bukkit.createInventory(null, 54,
                "§e§l" + player.getDisplayName() + "§e's settings");
    }

    private void updateGui(Player player) {
        PlayerSettings result = PlayerSettings.getSettings(player.getUniqueId());

        for (String key : result.getPlayerSetting().keySet()) {
            Setting set = result.getPlayerSetting().get(key);

            addSlot(Setting.getSlot(set, player, () -> {
                slots.remove(currentPage);
                slots.add(currentPage, new Page());
                updateGui(player);
            }));
        }
    }
}
