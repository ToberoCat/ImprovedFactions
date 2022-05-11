package io.github.toberocat.core.gui.faction;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.rank.members.AdminRank;
import io.github.toberocat.core.factions.rank.members.ElderRank;
import io.github.toberocat.core.factions.rank.members.MemberRank;
import io.github.toberocat.core.factions.rank.members.ModeratorRank;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.gui.GUISettings;
import io.github.toberocat.core.utility.gui.Gui;
import io.github.toberocat.core.utility.settings.type.EnumSetting;
import io.github.toberocat.core.utility.settings.type.Setting;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.Iterator;

public class ManagePlayerRankGui extends Gui {

    public static String[] RANKS = new String[] {
            AdminRank.registry,
            ModeratorRank.registry,
            ElderRank.registry,
            MemberRank.registry
    };

    private EnumSetting setting;
    private Faction faction;
    private OfflinePlayer managedPlayer;

    public ManagePlayerRankGui(Player player, OfflinePlayer managedPlayer, Faction faction, GUISettings manageSettings) {
        super(player, createInventory(player, managedPlayer), new GUISettings().setQuitIcon(true).setQuitCallback(() -> {
            new MemberManageGui(player, managedPlayer, faction, manageSettings);
        }));
        this.faction = faction;
        this.managedPlayer = managedPlayer;

        setting = new EnumSetting(RANKS, "swap_ranks", Utility.createItem(Material.ENDER_PEARL,
                "§eChange rank", new String[] { "§8Rank will only get updated after", "§8closing the inventory" }));
    }

    private void update(Player player, OfflinePlayer managedPlayer) {
        addSlot(Setting.getSlot(setting, player, () -> update(player, managedPlayer)), 0, 13);
    }

    @Override
    public void onInventoryClose(InventoryCloseEvent event, Iterator<Gui> iterator) {
        super.onInventoryClose(event, iterator);
        faction.getFactionPerm().setRank(managedPlayer, RANKS[setting.getSelected()]);
    }

    private static Inventory createInventory(Player player, OfflinePlayer managedPlayer) {
        return Bukkit.createInventory(player, 27, "§eManage §e§l" + managedPlayer.getName() + "§e's rank");
    }
}
