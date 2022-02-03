package io.github.toberocat.core.utility.gui.page;

import io.github.toberocat.core.utility.ObjectPair;
import io.github.toberocat.core.utility.gui.GUISettings;
import io.github.toberocat.core.utility.gui.slot.Slot;
import io.github.toberocat.core.utility.Utility;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class Page {
    public final static Integer[] FREE_SLOTS = new Integer[] {
            1, 10, 19, 28, 37,
            4, 13, 22, 31, 40,
            7, 16, 25, 34, 43
    };

    private int lastFreeIndexSlot;
    private Map<Integer, Slot> slots = new HashMap<>();

    public Page() {
        this.lastFreeIndexSlot = 0;
    }

    public void Render(int currentPage, int maxPage, Inventory inventory, GUISettings settings) {
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE));
        }

        for (int slotNumber : slots.keySet()) {
            Slot slot = slots.get(slotNumber);
            inventory.setItem(slotNumber, slot.getStack());
        }

        inventory.setItem(45, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        inventory.setItem(46, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        inventory.setItem(47, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        inventory.setItem(48, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        inventory.setItem(49, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        inventory.setItem(50, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        inventory.setItem(51, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        inventory.setItem(52, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        inventory.setItem(53, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));

        if (currentPage != 0) {
            inventory.setItem(45, Utility.createItem(Material.ARROW, "§c§lGo back", new String[] {
                    "&8Click to view", "&8the previous page"
            }));
        }
        if (currentPage != maxPage-1) {
            inventory.setItem(53, Utility.createItem(Material.ARROW, "§a§lNext page", new String[]{
                    "&8Click to view", "&8the next page"}));
        }

        if (settings.isQuitIcon()) {
            inventory.setItem(49, Utility.createItem(Material.BARRIER, "§c§lQuit page", new String[]{
                    "Quit to this page and", "go back to last view"
            }));
        }

        for (ObjectPair<Integer, Slot> slots : settings.getExtraSlots()) {
            inventory.setItem(slots.getT(), slots.getE().getStack());
        }

    }

    public int OnClick(InventoryClickEvent event, int currentPage, GUISettings settings) {

        for (ObjectPair<Integer, Slot> slots : settings.getExtraSlots()) {
            if (slots.getT() == event.getRawSlot()) {
                slots.getE().OnClick();
                break;
            }
        }

        if (slots.containsKey(event.getRawSlot())) {
            slots.get(event.getRawSlot()).OnClick();
        } else if (event.getRawSlot() == 45 && event.getCurrentItem().getType() == Material.ARROW) {
            return currentPage - 1;
        } else if (event.getRawSlot() == 53 && event.getCurrentItem().getType() == Material.ARROW) {
            return currentPage + 1;
        } else if (event.getRawSlot() == 49 && event.getCurrentItem().getType() == Material.BARRIER) {
            return -1;
        } else {
            return currentPage;
        }
        return currentPage;
    }

    public boolean AddSlot(Slot slot) {
        int slotForSlot = FREE_SLOTS[lastFreeIndexSlot++];

        if (!slots.containsKey(slotForSlot)) {
            slots.put(slotForSlot, slot);
        }

        return lastFreeIndexSlot > FREE_SLOTS.length-1 ? true : false;
    }

    public void AddSlot(Slot slot, int slotIndex) {
        if (!slots.containsKey(slotIndex)) {
            slots.put(slotIndex, slot);
        }
    }
}
