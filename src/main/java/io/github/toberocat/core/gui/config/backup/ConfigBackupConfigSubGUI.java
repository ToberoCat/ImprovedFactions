package io.github.toberocat.core.gui.config.backup;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.ObjectPair;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.config.Config;
import io.github.toberocat.core.utility.gui.Gui;
import io.github.toberocat.core.utility.gui.GUISettings;
import io.github.toberocat.core.utility.gui.slot.Slot;
import io.github.toberocat.core.utility.language.LangMessage;
import io.github.toberocat.core.utility.language.Language;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Arrays;

public class ConfigBackupConfigSubGUI extends Gui {
    public ConfigBackupConfigSubGUI(Player player, String config, ArrayList<String> backup) {
        super(player, Bukkit.createInventory(null, 54,
                Language.getMessage(LangMessage.GUI_BACKUP_CONFIG_TITLE, player) + " - " + config),
                new GUISettings().setQuitIcon(false));

        settings.getExtraSlots().add(new ObjectPair<>(48, new Slot(Utility.createItem(Material.RED_TERRACOTTA, "&c&lDelete backup", new String[] {
                "&8Remove the backup file", "if you finished checking", "all files"
        })) {
            @Override
            public void OnClick() {
                MainIF.getIF().getServer().getScheduler().runTaskLater(MainIF.getIF(), () -> {
                    MainIF.getIF().getBackupFile().remove(config);

                    new ConfigBackupGUI(player);
                }, 0);
            }
        }));

        settings.getExtraSlots().add(new ObjectPair<>(50, new Slot(Utility.createItem(Material.BARRIER, "§c§lQuit page", new String[]{
                "Quit to this page and", "go back to last view" })) {
            @Override
            public void OnClick() {
                MainIF.getIF().getServer().getScheduler().runTaskLater(MainIF.getIF(), () -> new ConfigBackupGUI(player), 1);
            }
        }));

        for (String configLine : backup) {
            String[] split = configLine.split(":");
            AddSlot(MainIF.getConfigManager().getConfig(split[0]).getItemIcon(), () -> {
                Inventory inv = Bukkit.createInventory(null, 54, Language.getMessage(LangMessage.GUI_BACKUP_CONFIG_TITLE, player) + " - " + config);
                final Gui gui = new Gui(player, inv, new GUISettings().setQuitIcon(true));
                gui.getSettings().setQuitCallback(() -> new ConfigBackupConfigSubGUI(player, config, backup));

                Config newConfig = MainIF.getConfigManager().getConfig(split[0]);

                String newDataRaw = WordUtils.wrap(newConfig.getValue().toString(), MainIF.getConfigManager().getValue("gui.wrapLength"));
                String[] newDataSplit = newDataRaw.split("\\r?\\n");

                String oldDataRaw = WordUtils.wrap(String.join("",
                        Arrays.copyOfRange(split, 1, split.length))
                        .replaceAll("\n", "<>"),
                        MainIF.getConfigManager().getValue("gui.wrapLength"));

                String[] oldDataSplit = oldDataRaw.split("<>");

                gui.AddSlot(Utility.createItem(Material.ENDER_EYE, "&a&lOld", oldDataSplit), 0, 10, () -> {

                });

                gui.AddSlot(Utility.createItem(Material.ENDER_EYE, "&a&lNew", newDataSplit), 0, 16, () -> {

                });
                gui.AddSlot(Utility.createItem(Material.LIME_DYE, "&a&lKeep new", new String[] {"&8Click to keep the newest changes", "&6&lWarning:&8 This remove the old backup"}), 0, 41, () -> {
                    MainIF.getIF().getBackupFile().get(config).remove(split[0]);
                    new ConfigBackupConfigSubGUI(player, config, backup);
                });
                gui.AddSlot(Utility.createItem(Material.YELLOW_DYE, "&a&lKeep old", new String[] {"&8Click to keep the backuped data", "&6&lWarning:&8 This remove the new changes"}), 0, 39, () -> {
                    MainIF.getConfigManager().getConfig(split[0]).write(split[1]);
                    new ConfigBackupConfigSubGUI(player, config, backup);
                });
            });
        }
    }
}
