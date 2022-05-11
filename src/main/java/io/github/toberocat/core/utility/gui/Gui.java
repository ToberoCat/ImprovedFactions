package io.github.toberocat.core.utility.gui;

import io.github.toberocat.core.listeners.GuiListener;
import io.github.toberocat.core.utility.callbacks.Callback;
import io.github.toberocat.core.utility.gui.page.Page;
import io.github.toberocat.core.utility.gui.slot.Slot;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Gui {

    protected boolean hasOpened;

    protected Inventory inventory;
    protected GUISettings settings;

    protected int currentPage;
    protected List<Page> slots;

    public Gui(Player player, Inventory inventory, GUISettings settings) {
        assert player != null && inventory != null && settings != null;

        GuiListener.GUIS.add(this);

        this.inventory = inventory;
        this.settings = settings;
        this.slots = new ArrayList<>();
        this.currentPage = 0;
        this.hasOpened = false;

        openInventory(player);

        slots.add(new Page());
        slots.get(0).render(0, slots.size(), inventory, settings);
    }

    private void openInventory(Player player) {
        player.openInventory(inventory);
    }

    public void onInventoryClick(InventoryClickEvent event, Iterator<Gui> iterator) {
        event.setCancelled(!settings.isClickable());
        int lastCurrentPage = currentPage;
        currentPage = slots.get(currentPage).OnClick(event, currentPage, settings);
        if (currentPage == -1) {
            settings.getQuitCallback().callback();
            currentPage = lastCurrentPage;
            return;
        }
        if (lastCurrentPage != currentPage) {
            slots.get(currentPage).render(currentPage, slots.size(), inventory, settings);
        }
    }

    protected void close() {
    }

    public void onInventoryDrag(InventoryDragEvent event, Iterator<Gui> iterator) {
        event.setCancelled(!settings.isDragable());
    }

    public void onInventoryClose(InventoryCloseEvent event, Iterator<Gui> iterator) {
        if (event.getInventory().equals(inventory)) {
            close();
            iterator.remove();
        }
    }

    public void clear() {
        slots.remove(currentPage);
        slots.add(currentPage, new Page());
    }

    public void addSlot(Slot slot) {
        if (slot == null) return;
        if (slots.get(slots.size() - 1).addSlot(slot)) slots.add(new Page());

        slots.get(currentPage).render(currentPage, slots.size(), inventory, settings);
    }

    public void addSlot(Slot slot, int page, int invSlot) {
        slots.get(page).addSlot(slot, invSlot);
        slots.get(currentPage).render(currentPage, slots.size(), inventory, settings);
    }

    public void addSlot(ItemStack stack, Callback callback) {
        if (slots.get(slots.size() - 1).addSlot(new Slot(stack) {
            @Override
            public void OnClick(HumanEntity entity) {
                callback.callback();
            }
        })) {
            slots.add(new Page());
        }
        slots.get(currentPage).render(currentPage, slots.size(), inventory, settings);
    }

    public void addSlot(ItemStack stack, int page, int slot, Callback callback) {
        slots.get(page).addSlot(new Slot(stack) {
            @Override
            public void OnClick(HumanEntity entity) {
                callback.callback();
            }
        }, slot);
        slots.get(currentPage).render(currentPage, slots.size(), inventory, settings);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public GUISettings getSettings() {
        return settings;
    }

    public void setSettings(GUISettings settings) {
        this.settings = settings;
    }
}
