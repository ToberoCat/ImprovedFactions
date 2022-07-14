package io.github.toberocat.core.gui.faction;

import io.github.toberocat.core.factions.local.LocalFaction;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.async.AsyncTask;
import io.github.toberocat.core.utility.gui.TabbedGui;
import io.github.toberocat.core.utility.gui.settings.GuiSettings;
import io.github.toberocat.core.utility.settings.type.RankSetting;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class RankGui extends TabbedGui {

    private final Runnable close;

    public RankGui(Player player, LocalFaction faction, Runnable close) {
        super(player, createInventory(player, faction));
        this.close = close;
        update(player, faction);
    }

    @Override
    protected GuiSettings readSettings() {
        return super.readSettings().setQuitGui(close);
    }

    private static Inventory createInventory(Player player, LocalFaction faction) {
        return Bukkit.createInventory(player, 54, "§e§l" +
                faction.getDisplayName() + " permissions");
    }

    private void update(Player player, LocalFaction faction) {
        for (String key : faction.getFactionPerm().getRankSetting().keySet()) {
            RankSetting setting = faction.getFactionPerm().getRankSetting().get(key);
            ItemStack stack = setting.getDisplay();
            if (stack == null) stack = Utility.createItem(Material.GRASS_BLOCK, "Undefined setting");
            List<String> lore = Utility.getLore(stack);
            lore.add("");
            lore.add("§8Click to manage");

            addSlot(Utility.setLore(stack, lore.toArray(String[]::new)), (user) ->
                    AsyncTask.runLaterSync(0,
                            () -> new ManageRankSettingGui(player, faction, key, setting, this.settings)));
        }

        render();
    }
}
