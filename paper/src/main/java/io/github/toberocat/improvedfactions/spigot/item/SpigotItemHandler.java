package io.github.toberocat.improvedfactions.spigot.item;

import io.github.toberocat.improvedFactions.core.handler.ItemHandler;
import io.github.toberocat.improvedFactions.core.item.ItemStack;
import io.github.toberocat.improvedfactions.spigot.utils.ItemUtils;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;

public class SpigotItemHandler implements ItemHandler {

    public static Material fromMaterial(@NotNull String material) {
        return Material.valueOf(material.split("minecraft:")[1].toUpperCase());
    }

    @Override
    public @NotNull ItemStack createStack(@NotNull String material, @NotNull String title,
                                          int quantity, String... lore) {
        return new SpigotItemStack(ItemUtils.createItem(fromMaterial(material),
                quantity,
                title,
                lore));
    }

    @Override
    public @NotNull ItemStack createSkull(@NotNull URL textureId, @NotNull String title, String... lore) {
        return new SpigotItemStack(ItemUtils.createHead(textureId.getPath(), title, 1, lore));
    }

    @Override
    public @NotNull ItemStack fromBase64(@NotNull String data) throws IOException {
        org.bukkit.inventory.ItemStack stack = ItemUtils.itemFromBase64(data);
        if (stack == null) throw new IOException();
        return new SpigotItemStack(stack);
    }

}
