package io.github.toberocat.core.gui.faction;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.factions.local.LocalFaction;
import io.github.toberocat.core.listeners.PlayerJoinListener;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.gui.TabbedGui;
import io.github.toberocat.core.utility.gui.settings.GuiSettings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class OnlineGUI extends TabbedGui {
    private final int task;
    private final Runnable close;

    public OnlineGUI(Player player, LocalFaction faction, Runnable close) {
        super(player, createInv(faction, player));
        this.close = close;

        task = Bukkit.getScheduler().runTaskTimer(MainIF.getIF(), () -> update(player, faction), 0, 20).getTaskId();
    }

    @Override
    protected GuiSettings readSettings() {
        return super.readSettings().setQuitGui(close);
    }

    private static Inventory createInv(LocalFaction faction, Player player) {
        return Bukkit.createInventory(player, 54, "§e" + faction.getDisplayName() + "'s online members");
    }

    @Override
    protected void inventoryClosed(@NotNull InventoryCloseEvent event) {
        Bukkit.getScheduler().cancelTask(task);
    }

    private void update(Player player, LocalFaction faction) {
        clear();

        for (Player online : faction.getFactionMemberManager().getOnlinePlayers()) {
            String time = Utility.getTime(PlayerJoinListener.PLAYER_JOINS.get(online.getUniqueId()));
            String totalTime = time + "§8 online";
            addSlot(Utility.getSkull(online, 1, player.getDisplayName(),
                    new String[]{"§8Player is now §e" + totalTime}), (user) -> {
            });
        }

        render();
    }
}
