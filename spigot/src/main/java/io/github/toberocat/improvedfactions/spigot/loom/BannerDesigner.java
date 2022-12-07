package io.github.toberocat.improvedfactions.spigot.loom;

import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.core.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.gui.manager.GuiManager;
import io.github.toberocat.improvedFactions.core.gui.content.GuiContent;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedfactions.spigot.MainIF;
import io.github.toberocat.improvedfactions.spigot.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class BannerDesigner implements Listener { // ToDo: Fix Faction Icon designer

    private static final List<BannerDesigner> openedInstances = new LinkedList<>();

    private final Player player;

    private final MainIF mainIF;

    private final ItemStack[] oldPlayerInventory;
    private final Inventory loomInventory;

    public BannerDesigner(@NotNull FactionPlayer<?> player,
                          @NotNull MainIF mainIF)
            throws FactionNotInStorage, PlayerHasNoFactionException {
        openedInstances.add(this);

        this.player = (Player) player.getRaw();
        Faction<?> faction = player.getFaction();
        oldPlayerInventory = this.player.getInventory().getContents();
        this.mainIF = mainIF;

        loomInventory = createInventory();
        cloneJsonGui(player);

        Bukkit.getScheduler().runTaskLater(mainIF, () -> loomInventory
                .setItem(0, Objects.requireNonNullElse((ItemStack) faction.getIcon().getRaw(),
                        ItemUtils.createItem(Material.WHITE_BANNER, faction.getDisplay()))), 1);

        mainIF.getServer().getPluginManager().registerEvents(this, mainIF);
        this.player.openInventory(loomInventory);
    }

    public static void dispose() {
        new ArrayList<>(openedInstances)
                .forEach(BannerDesigner::resetInventory);
        openedInstances.clear();
    }

    private @NotNull Inventory createInventory() {
        return Bukkit.createInventory(player, InventoryType.LOOM);
    }

    private void cloneJsonGui(@NotNull FactionPlayer<?> player) {
        this.player.getInventory().clear();

/*        guiContent.getContent().forEach((container, action) -> {
            ItemStack stack = (ItemStack) container.stack().getRaw();
            ItemUtils.translateItem(sender, guiContent.getGuiId(), stack);

            this.sender.getInventory().setItem(9 + container.slot(), stack);
        });*/
    }

    @EventHandler
    private void inventoryClick(InventoryClickEvent event) {
        if (!event.getWhoClicked().getUniqueId().equals(player.getUniqueId())) return;

        if (event.getClickedInventory() != loomInventory) {
            ItemStack current = event.getCurrentItem();
            ItemStack cursor = event.getCursor();
            event.setCancelled(true);

            if (cursor != null && cursor.getType() != Material.AIR) Bukkit.getScheduler()
                    .runTaskLater(mainIF, () -> event.getView().setCursor(null), 0);
            else if (current != null && current.getType() != Material.AIR) Bukkit.getScheduler()
                    .runTaskLater(mainIF, () -> event.getView().setCursor(current), 0);

            return;
        }


        System.out.println(event.getSlot() + "; " + event.getRawSlot());
    }

    @EventHandler
    private void closeInv(InventoryCloseEvent event) {
        if (event.getInventory() != loomInventory) return;
        if (!event.getPlayer().getUniqueId().equals(player.getUniqueId())) return;
        resetInventory();
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
        resetInventory();
    }

    private void resetInventory() {
        openedInstances.remove(this);
        player.closeInventory();
        player.getInventory().clear();
        player.getInventory().setContents(oldPlayerInventory);
        HandlerList.unregisterAll(this);
    }
}
