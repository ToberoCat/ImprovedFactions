package io.github.toberocat.improvedfactions.listener;

import io.github.toberocat.improvedfactions.wand.PositionWand;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class InventoryListener implements Listener {
    @EventHandler
    public void click(InventoryClickEvent event) {
        HumanEntity player = event.getWhoClicked();

        if (!PositionWand.USER_ITEMS.contains(player.getUniqueId())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void drag(InventoryDragEvent event) {
        HumanEntity player = event.getWhoClicked();

        if (!PositionWand.USER_ITEMS.contains(player.getUniqueId())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void drop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        if (!PositionWand.USER_ITEMS.contains(player.getUniqueId())) return;
        event.setCancelled(true);
    }
}
