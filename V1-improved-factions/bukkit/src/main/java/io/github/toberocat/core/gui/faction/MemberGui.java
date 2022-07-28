package io.github.toberocat.core.gui.faction;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.async.AsyncTask;
import io.github.toberocat.core.utility.gui.TabbedGui;
import io.github.toberocat.core.utility.gui.settings.GuiSettings;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class MemberGui extends TabbedGui {
    public MemberGui(Player player, Faction<?> faction, GuiSettings settings) {
        super(player, createInventory(player, faction));

        faction.getMembers().forEach(uuid -> {
            OfflinePlayer off = Bukkit.getOfflinePlayer(uuid);
            String lastTimeSeen = off.isOnline() ? "§aOnline" : "§e" + Utility.getTime(off.getLastPlayed());

            addSlot(Utility.getSkull(off, 1, "§e" + off.getName(), new String[]{
                    "§8Last time seen: " + lastTimeSeen,
                    "§8Rank: §e" + faction.getPlayerRank(off).getDisplayName()
            }), (user) -> AsyncTask.runLaterSync(1, () ->
                    new MemberManageGui(player, off, faction, settings)));
        });

        render();
    }

    private static Inventory createInventory(Player player, Faction<?> faction) {
        return Bukkit.createInventory(player, 54, "§e" + faction.getDisplay() + "'s members");
    }
}
