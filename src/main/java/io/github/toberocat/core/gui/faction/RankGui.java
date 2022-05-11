package io.github.toberocat.core.gui.faction;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.async.AsyncTask;
import io.github.toberocat.core.utility.gui.GUISettings;
import io.github.toberocat.core.utility.gui.Gui;
import io.github.toberocat.core.utility.settings.type.RankSetting;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class RankGui extends Gui {
    public RankGui(Player player, Faction faction, GUISettings settings) {
        super(player, createInventory(player, faction), settings);
        update(player, faction);
    }

    private static Inventory createInventory(Player player, Faction faction) {
        return Bukkit.createInventory(player, 54, "§e§l" + faction.getDisplayName() + " permissions");
    }

    private void update(Player player, Faction faction) {
        for (String key : faction.getFactionPerm().getRankSetting().keySet()) {
            RankSetting setting = faction.getFactionPerm().getRankSetting().get(key);
            ItemStack stack = setting.getDisplay();
            List<String> lore = Utility.getLore(stack);
            lore.add("");
            lore.add("§8Click to manage");

            addSlot(Utility.setLore(stack, lore.toArray(String[]::new)), () -> AsyncTask.runLaterSync(0,
                    () -> new ManageRankSettingGui(player, faction, key, setting, this.settings)));
        }
    }
}
