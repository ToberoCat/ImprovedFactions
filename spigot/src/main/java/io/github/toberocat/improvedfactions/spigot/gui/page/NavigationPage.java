package io.github.toberocat.improvedfactions.spigot.gui.page;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class NavigationPage extends Page {
    public NavigationPage(int inventorySize) {
        super(inventorySize, slot -> {});
    }

    @Override
    public void render(Inventory inventory) {
        inventory.clear();

        for (int i = inventory.getSize() - 9; i < inventory.getSize(); i++)
            inventory.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));

        renderSlots(inventory);
    }
}
