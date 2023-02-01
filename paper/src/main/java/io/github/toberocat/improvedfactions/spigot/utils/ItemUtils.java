package io.github.toberocat.improvedfactions.spigot.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import io.github.toberocat.improvedFactions.core.handler.message.MessageHandler;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.translator.layout.item.XmlItem;
import io.github.toberocat.improvedFactions.core.utils.Logger;
import io.github.toberocat.improvedfactions.spigot.MainIF;
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
import java.lang.reflect.Field;
import java.util.*;

public class ItemUtils {

    public static final NamespacedKey ORIGINAL_NAME_KEY = new NamespacedKey(JavaPlugin.getPlugin(MainIF.class),
            "original_naming");

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

    public static void translateItem(@NotNull FactionPlayer<?> player,
                                     @NotNull String guiId,
                                     @NotNull ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();
        if (meta == null) return;
        meta.getPersistentDataContainer()
                .set(ORIGINAL_NAME_KEY, PersistentDataType.STRING,
                        meta.getDisplayName());

        String id = meta.getDisplayName();
        MessageHandler api = MessageHandler.api();
        String title = player.getMessage(translatable -> {
            XmlItem xml = translatable.getGuis()
                    .get(guiId)
                    .get(id);
            if (xml != null) return xml.title();
            Logger.api().logWarning(id + " has missing title");
            return null;
        });
        meta.setDisplayName(api.format(player, Objects.requireNonNullElse(title, "")));
        meta.setLore(Arrays.stream(player.getMessageBatch(translatable -> translatable.getGuis()
                        .get(guiId)
                        .get(id).description()
                        .stream()
                        .map(x -> api.format(player, x))
                        .toArray(String[]::new)))
                .toList());

        stack.setItemMeta(meta);
    }

    public static void resetTranslation(@Nullable ItemStack stack) {
        if (stack == null) return;

        ItemMeta meta = stack.getItemMeta();
        if (meta == null) return;

        String old = meta.getPersistentDataContainer()
                .get(ORIGINAL_NAME_KEY, PersistentDataType.STRING);
        if (old == null) return;

        meta.getPersistentDataContainer()
                .remove(ORIGINAL_NAME_KEY);

        meta.setDisplayName(old);
        meta.setLore(List.of());
        stack.setItemMeta(meta);
    }

    public static ItemStack setLore(ItemStack stack, String[] lore) {
        ItemStack newStack = new ItemStack(stack);
        ItemMeta meta = newStack.getItemMeta();
        assert meta != null;
        meta.setLore(Arrays.stream(lore).map(x -> MessageHandler.api().format(x)).toList());
        newStack.setItemMeta(meta);
        return newStack;
    }

    public static ItemStack createItem(Material material, String name) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        meta.setDisplayName(MessageHandler.api().format(name));
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack createItem(Material material, int amount, String name, String... lore) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(MessageHandler.api().format(name));

            meta.setLore(Objects.requireNonNull(setLore(item, lore).getItemMeta()).getLore());

            item.setItemMeta(meta);
        }
        return item;
    }

    public static @NotNull ItemStack createHead(@NotNull String textureId,
                                                int amount,
                                                @NotNull String title,
                                                String[] lore) {
        System.out.println("G");
        ItemStack head = createItem(Material.PLAYER_HEAD, amount, title, lore);
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), "");

        profile.getProperties().put("textures", new Property("texture",
                "https://textures.minecraft.net/texture/" + textureId));
        try {
            Field profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }

        head.setItemMeta(headMeta);
        return head;
    }
}
