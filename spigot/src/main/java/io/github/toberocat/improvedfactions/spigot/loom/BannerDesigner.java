/**
 * Created: 01/10/2022
 *
 * @author Tobias Madlberger (Tobias)
 */

package io.github.toberocat.improvedfactions.spigot.loom;

import io.github.toberocat.improvedFactions.core.gui.GuiManager;
import io.github.toberocat.improvedFactions.core.gui.JsonGui;
import io.github.toberocat.improvedfactions.spigot.MainIF;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BannerDesigner implements Listener {
    private final Player player;
    private final ItemStack[] oldPlayerInventory;
    private final Inventory loomInventory;

    public BannerDesigner(@NotNull Player player, @NotNull MainIF mainIF) {
        this.player = player;
        this.oldPlayerInventory = player.getInventory().getContents();
        mainIF.getServer().getPluginManager().registerEvents(this, mainIF);
        loomInventory = Bukkit.createInventory(player, InventoryType.LOOM);

        player.getInventory().clear();

        JsonGui jsonGui = GuiManager.getGui(GuiManager.PLAYER_ICON_DESIGNER_INV);
        jsonGui.getContent().forEach((container, action) -> {
            player.getInventory().setItem(9 + container.slot(), (ItemStack) container.stack().getRaw());
        });
        player.openInventory(loomInventory);
    }

    public void setBanner(@NotNull ItemStack banner) {
        loomInventory.setItem(0, banner);
    }

    @EventHandler
    private void inventoryClick(InventoryClickEvent event) {
        if (event.getInventory() != loomInventory) return;
        if (!event.getWhoClicked().getUniqueId().equals(player.getUniqueId())) return;

        System.out.println(event.getSlot() + "; " + event.getRawSlot());
    }

    @EventHandler
    private void closeInv(InventoryCloseEvent event) {
        if (event.getInventory() != loomInventory) return;
        if (!event.getPlayer().getUniqueId().equals(player.getUniqueId())) return;

        player.getInventory().clear();
        player.getInventory().setContents(oldPlayerInventory);
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    private void dropEvent(PlayerDropItemEvent event) {
        if (!loomInventory.getViewers().contains(event.getPlayer())) return;
        event.setCancelled(true);
    }

    @EventHandler
    private void pickupEvent(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!loomInventory.getViewers().contains(player)) return;
        event.setCancelled(true);
    }

    @EventHandler
    private void death(PlayerDeathEvent event) {
        if (!event.getEntity().getUniqueId().equals(player.getUniqueId())) return;
        event.getDrops().clear();
    }

    @EventHandler
    private void respawn(PlayerRespawnEvent event) {
        if (event.getPlayer().getUniqueId().equals(player.getUniqueId())) return;

        player.getInventory().clear();
        player.getInventory().setContents(oldPlayerInventory);
        HandlerList.unregisterAll(this);
    }
}
