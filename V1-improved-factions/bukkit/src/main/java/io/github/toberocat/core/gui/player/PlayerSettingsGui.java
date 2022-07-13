package io.github.toberocat.core.gui.player;

import io.github.toberocat.core.utility.gui.TabbedGui;
import io.github.toberocat.core.player.PlayerSettings;
import io.github.toberocat.core.utility.settings.type.Setting;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class PlayerSettingsGui extends TabbedGui {

    public PlayerSettingsGui(Player player) {
        super(player, createInventory(player));
        updateGui(player);
    }

    private static Inventory createInventory(Player player) {
        return Bukkit.createInventory(null, 54,
                "§e§l" + player.getDisplayName() + "§e's settings");
    }

    private void updateGui(Player player) {
        clear();

        PlayerSettings result = PlayerSettings.getSettings(player.getUniqueId());
        for (String key : result.getPlayerSetting().keySet()) {
            Setting set = result.getPlayerSetting().get(key);

            addSlot(Setting.getSlot(set, player, () -> updateGui(player)));
        }

        render();
    }
}
