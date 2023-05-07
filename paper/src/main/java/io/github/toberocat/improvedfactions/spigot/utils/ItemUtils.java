package io.github.toberocat.improvedfactions.spigot.utils;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import io.github.toberocat.improvedFactions.core.handler.message.MessageHandler;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.utils.Logger;
import io.github.toberocat.improvedfactions.spigot.MainIF;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

import static io.github.toberocat.improvedfactions.spigot.utils.ComponentUtility.format;

public class ItemUtils {

    public static String itemToBase64(ItemStack item) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeObject(item.serialize());
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stack", e);
        }
    }

    public static @Nullable ItemStack itemFromBase64(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

            Map<String, Object> stack = (Map<String, Object>) dataInput.readObject();
            ItemStack item = stack == null ? null : ItemStack.deserialize(stack);
            dataInput.close();
            return item;
        } catch (ClassNotFoundException | NullPointerException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }

    public static ItemStack createItem(Material material, int amount, String name, String... lore) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        if (meta == null)
            return item;

        meta.displayName(format(name));
        meta.lore(Arrays.stream(lore).map(ComponentUtility::format).toList());
        item.setItemMeta(meta);

        return item;
    }

    public static @NotNull ItemStack createHead(@NotNull String textureId,
                                                @NotNull String title,
                                                int amount,
                                                String[] lore) {
        ItemStack head = createItem(Material.PLAYER_HEAD, amount, title, lore);
        if (!(head.getItemMeta() instanceof SkullMeta headMeta))
            return head;

        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID(), "");
        profile.setProperty(new ProfileProperty("textures", textureId));
        headMeta.setPlayerProfile(profile);

        head.setItemMeta(headMeta);
        return head;
    }
}
