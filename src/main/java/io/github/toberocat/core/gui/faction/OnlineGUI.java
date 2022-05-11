package io.github.toberocat.core.gui.faction;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.listeners.PlayerJoinListener;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.gui.GUISettings;
import io.github.toberocat.core.utility.gui.Gui;
import io.github.toberocat.core.utility.gui.page.Page;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class OnlineGUI extends Gui {
    private final int task;

    public OnlineGUI(Player player, Faction faction, GUISettings settings) {
        super(player, createInv(faction, player), settings);

        task = Bukkit.getScheduler().runTaskTimer(MainIF.getIF(), () -> update(player, faction), 0, 20).getTaskId();
    }

    private static Inventory createInv(Faction faction, Player player) {
        return Bukkit.createInventory(player, 54, "§e" + faction.getDisplayName() + "'s online members");
    }

    @Override
    protected void close() {
        Bukkit.getScheduler().cancelTask(task);
    }

    private void update(Player player, Faction faction) {
        slots.remove(currentPage);
        slots.add(currentPage, new Page());

        for (Player online : faction.getFactionMemberManager().getOnlinePlayers()) {
            String time = Utility.getTime(PlayerJoinListener.PLAYER_JOINS.get(online.getUniqueId()));
            String totalTime = time + "§8 online";
            addSlot(Utility.getSkull(online, 1, player.getDisplayName(),
                    new String[]{"§8Player is now §e" + totalTime}), () -> {
            });
        }
    }
}
