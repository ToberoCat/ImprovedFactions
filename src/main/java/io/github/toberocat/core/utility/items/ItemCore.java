package io.github.toberocat.core.utility.items;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.config.DataManager;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemCore {

    private static final DataManager ITEMS = new DataManager(MainIF.getIF(), "items.yml");

    public static void register() {
    }

    public static ItemStack create(String path, Material material, String title, String... lore) {
        return Utility.createItem(material, title, lore);
    }
}
