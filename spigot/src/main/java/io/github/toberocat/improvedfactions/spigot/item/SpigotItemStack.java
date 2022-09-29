package io.github.toberocat.improvedfactions.spigot.item;

import io.github.toberocat.improvedFactions.core.item.ItemStack;
import io.github.toberocat.improvedfactions.spigot.utils.ItemUtils;
import org.jetbrains.annotations.NotNull;

public class SpigotItemStack implements ItemStack {

    private final org.bukkit.inventory.ItemStack itemStack;

    public SpigotItemStack(@NotNull org.bukkit.inventory.ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public @NotNull String toBase64() {
        return ItemUtils.itemToBase64(itemStack);
    }

    @Override
    public @NotNull org.bukkit.inventory.ItemStack getRaw() {
        return itemStack;
    }
}
