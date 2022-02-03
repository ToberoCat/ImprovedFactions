package io.github.toberocat.core.gui.settings;

import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.gui.GUISettings;
import io.github.toberocat.core.utility.gui.Gui;
import io.github.toberocat.core.utility.gui.page.Page;
import io.github.toberocat.core.utility.language.LangMessage;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.language.Parseable;
import io.github.toberocat.core.utility.settings.PlayerSettings;
import io.github.toberocat.core.utility.Result;
import io.github.toberocat.core.utility.settings.Setting;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.List;

public class PlayerSettingsGui extends Gui {

    public PlayerSettingsGui(Player player) {
        super(player, createInventory(player), new GUISettings());
        updateGui(player);
    }

    private void updateGui(Player player) {
        Result<PlayerSettings> result =  PlayerSettings.getSettings(player.getUniqueId());

        if (!result.isSuccess()) {
            Language.sendMessage(LangMessage.ERROR_GENERAL, player, new Parseable("{error}",
                    result.getPlayerMessage()));
        }

        for (String key : result.getPaired().getPlayerSetting().keySet()) {
            Setting set = result.getPaired().getPlayerSetting().get(key);
            switch (set.getType()) {
                case BOOL -> {
                    String enabled = "&a";
                    String disabled = "&c";
                    if ((Boolean) set.getSelected()) enabled += "&n";
                    else disabled += "&n";
                    AddSlot(Utility.createItem(set.getMaterial(), "&e" + key, new String[]{
                            "&7Type: &eBoolean",
                            enabled + "enabled",
                            disabled + "disabled"
                    }), () -> {
                        set.setSelected(!(Boolean) set.getSelected());
                        slots.remove(currentPage);
                        slots.add(currentPage, new Page());
                        updateGui(player);
                    });
                }
                case ENUM -> {
                    String[] values = set.getEnumValues();
                    String[] lore = new String[values.length + 1];
                    lore[0] = "&7Type: &eSelector";
                    for (int i = 1; i < lore.length; i++) {
                        lore[i] = (set.getSelected().toString().equals(values[i - 1]) ? "&f" : "&7") +
                                values[i - 1];
                    }
                    AddSlot(Utility.createItem(set.getMaterial(), "&e" + key, lore), () -> {
                        set.setSelected(values[getNextSelected(Arrays.asList(values), set)]);
                        slots.remove(currentPage);
                        slots.add(currentPage, new Page());
                        updateGui(player);
                    });
                }
            }
        }
    }

    private int getNextSelected(List<String> values, Setting set) {
        int index = values.indexOf(set.getSelected()) + 1;
        if (index > values.size() - 1) {
            index = 0;
        }
        return index;
    }



    private static Inventory createInventory(Player player) {
        return Bukkit.createInventory(null, 54,
                "§e§l"+player.getDisplayName() + "§e's settings");
    }
}
