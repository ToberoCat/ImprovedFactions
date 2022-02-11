package io.github.toberocat.core.gui.faction;

import io.github.toberocat.core.listeners.PlayerJoinListener;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.factions.Faction;
import io.github.toberocat.core.utility.factions.FactionUtility;
import io.github.toberocat.core.utility.gui.GUISettings;
import io.github.toberocat.core.utility.gui.Gui;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class OnlineGUI extends Gui {
    public OnlineGUI(Player player, Faction faction, GUISettings settings) {
        super(player, createInv(faction, player), settings);
        for (Player online : faction.getFactionMemberManager().getOnlinePlayers()) {
            String time = Utility.getTime(PlayerJoinListener.PLAYER_JOINS.get(online.getUniqueId()));
            String totalTime = time+"§8 online";
            AddSlot(Utility.getSkull(online, 1, player.getDisplayName(),
                    new String[] {"§8Player is now §e"+totalTime}), () -> {});
        }
    }

    private static Inventory createInv(Faction faction, Player player) {
        return Bukkit.createInventory(player, 54, "§e"+faction.getDisplayName() + "'s online members");
    }
}
