package io.github.toberocat.core.utility.gui.page;

import io.github.toberocat.core.utility.ObjectPair;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.gui.GUISettings;
import io.github.toberocat.core.utility.gui.slot.Slot;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class Page {
    public final static Integer[] FREE_SLOTS = new Integer[]{
            1, 10, 19, 28, 37,
            4, 13, 22, 31, 40,
            7, 16, 25, 34, 43
    };
    private final Map<Integer, Slot> slots = new HashMap<>();
    private int lastFreeIndexSlot;

    public Page() {
        this.lastFreeIndexSlot = 0;
    }

    public void render(int currentPage, int maxPage, Inventory inventory, GUISettings settings) {
        int lastRowStart = inventory.getSize() - 9;
        int lastRowEnd = inventory.getSize() - 1;

        for (int slotNumber : slots.keySet()) {
            Slot slot = slots.get(slotNumber);
            inventory.setItem(slotNumber, slot.getStack());
        }

        for (int i = lastRowStart; i <= lastRowEnd; i++) {
            inventory.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        }

        if (currentPage != 0) {
            inventory.setItem(lastRowStart, Utility.createItem(Material.ARROW, "§c§lGo back", new String[]{
                    "&8Click to view", "&8the previous page"
            }));
        }
        if (currentPage != maxPage - 1) {
            inventory.setItem(lastRowEnd, Utility.createItem(Material.ARROW, "§a§lNext page", new String[]{
                    "&8Click to view", "&8the next page"}));
        }

        if (settings.isQuitIcon()) {
            inventory.setItem(lastRowStart + 4, Utility.createItem(Material.BARRIER, "§c§lQuit page", new String[]{
                    "&8Quit to this page and", "&8go back to last view"
            }));
        }

        for (ObjectPair<Integer, Slot> slots : settings.getExtraSlots()) {
            inventory.setItem(slots.getT(), slots.getE().getStack());
        }
    }

    public int OnClick(InventoryClickEvent event, int currentPage, GUISettings settings) {
        if (event.getClickedInventory() == null) return currentPage;

        for (ObjectPair<Integer, Slot> slots : settings.getExtraSlots()) {
            if (slots.getT() == event.getRawSlot()) {
                slots.getE().OnClick(event.getWhoClicked());
                return currentPage;
            }
        }

        int lastRowStart = event.getClickedInventory().getSize() - 9;
        int lastRowEnd = event.getClickedInventory().getSize() - 1;

        if (slots.containsKey(event.getRawSlot())) {
            slots.get(event.getRawSlot()).OnClick(event.getWhoClicked());
        } else if (event.getRawSlot() == lastRowStart && event.getCurrentItem().getType() == Material.ARROW) {
            return currentPage - 1;
        } else if (event.getRawSlot() == lastRowEnd && event.getCurrentItem().getType() == Material.ARROW) {
            return currentPage + 1;
        } else if (event.getRawSlot() == lastRowStart + 4 && event.getCurrentItem().getType() == Material.BARRIER) {
            return -1;
        }
        return currentPage;
    }

    public boolean addSlot(Slot slot) {
        int slotForSlot = FREE_SLOTS[lastFreeIndexSlot];
        lastFreeIndexSlot++;

        if (!slots.containsKey(slotForSlot)) {
            slots.put(slotForSlot, slot);
        }

        return lastFreeIndexSlot > FREE_SLOTS.length - 1;
    }

    public void addSlot(Slot slot, int slotIndex) {
        if (!slots.containsKey(slotIndex)) {
            slots.put(slotIndex, slot);
        }
    }
}
