package io.github.toberocat.improvedfactions.spigot.item;

import io.github.toberocat.improvedFactions.core.item.ItemStack;
import io.github.toberocat.improvedfactions.spigot.utils.ItemUtils;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public record SpigotItemStack(org.bukkit.inventory.ItemStack itemStack) implements ItemStack {

    public SpigotItemStack(org.bukkit.inventory.ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public @NotNull String toBase64() {
        if (itemStack == null) return "null";
        return ItemUtils.itemToBase64(itemStack);
    }

    @Override
    public @NotNull org.bukkit.inventory.ItemStack getRaw() {
        return itemStack;
    }

    @Override
    public void setName(@NotNull String name) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) meta.setDisplayName(name);
        itemStack.setItemMeta(meta);
    }

    @Override
    public void setLore(@NotNull String... lore) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) meta.setLore(Arrays.stream(lore).toList());
        itemStack.setItemMeta(meta);
    }
}
