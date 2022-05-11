package io.github.toberocat.core.gui.config.configure;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.config.Config;
import io.github.toberocat.core.utility.gui.GUISettings;
import io.github.toberocat.core.utility.gui.Gui;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class ConfigConfigureGUI extends Gui {

    public ConfigConfigureGUI(Player player, String configFile) {
        super(player, createInventory(configFile), new GUISettings());

        for (Config config : MainIF.getIF().getConfigMap().values()) {
            if (config.getManager().getFileName().equals(configFile)) {
                addSlot(config.getItemIcon(), () -> {
                    //ToDo: Add NMS book opening
                });
            }
        }
    }

    private static Inventory createInventory(String config) {
        return Bukkit.createInventory(null, 54, "§e§lConfig - " + config);
    }
}
