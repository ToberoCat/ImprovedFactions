package io.github.toberocat.core.gui.config.backup;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.gui.GUISettings;
import io.github.toberocat.core.utility.gui.Gui;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class ConfigBackupGUI extends Gui {

    public ConfigBackupGUI(Player player) {
        super(player, Bukkit.createInventory(null, 54,
                Language.getMessage("gui.config.backup.title", player)), new GUISettings());
        for (String configFile : MainIF.getIF().getBackupFile().keySet()) {
            ArrayList<String> backUpData = MainIF.getIF().getBackupFile().get(configFile);

            addSlot(MainIF.getConfigManager().getDataManager(configFile.split("_")[0]).getItemIcon(),
                    () -> new ConfigBackupConfigSubGUI(player, configFile, backUpData));
        }
    }
}
