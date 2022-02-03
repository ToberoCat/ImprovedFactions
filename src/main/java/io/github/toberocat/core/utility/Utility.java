package io.github.toberocat.core.utility;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.callbacks.ExceptionCallback;
import io.github.toberocat.core.utility.events.faction.FactionCreateEvent;
import io.github.toberocat.core.utility.events.faction.FactionEvent;
import io.github.toberocat.core.utility.events.faction.FactionEventCancelledable;
import io.github.toberocat.core.utility.factions.Faction;
import io.github.toberocat.core.utility.language.Language;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class Utility {

    /**
     * Removed the first element in list and returns it
     * @param list the list where the item should be removed
     * @param <T> Type of the list
     * @return A objectPair. First object in there is the shifted element and them second is the new list
     */
    public static <T> ObjectPair<T, T[]> shift(T[] list) {
        List<T> t = Arrays.asList(list);
        MainIF.LogMessage(Level.INFO, ""+list.length);
        T tShift = t.remove(0);
        return new ObjectPair<>(tShift, toArray(t));
    }

    /**
     * Make a generic list to an array
     * @param list for convertion
     * @param <T> Type of the list
     * @return The list as an array
     */
    public static <T> T[] toArray(List<T> list) {
        T[] toR = (T[]) java.lang.reflect.Array.newInstance(list.get(0)
                .getClass(), list.size());
        for (int i = 0; i < list.size(); i++) {
            toR[i] = list.get(i);
        }
        return toR;
    }

    /**
     * Create a item with a simple formatted name
     * @param material The material for this item
     * @param name The title. E.g: "&e&lFactionItem"
     * @return itemstack with a custom title and your set material
     */
    public static ItemStack createItem(final Material material, final String name) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(Language.format(name));

        item.setItemMeta(meta);

        return item;
    }

    /**
     * Create a item with a simple formatted name and lore as array
     * @param material The material for this item
     * @param name The title. E.g: "&e&lFactionItem"
     * @param lore The lore the item should have
     * @return itemstack with a custom title, custom lore and your set material
     */
    public static ItemStack createItem(final Material material, final String name, final String[] lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(Language.format(name));

        SetLore(lore, meta);

        item.setItemMeta(meta);

        return item;
    }


    private static void SetLore(String[] lore, ItemMeta meta) {
        if (lore != null) {
            List<String> formatLore = new ArrayList<>();

            for (String s : lore) {
                formatLore.add(Language.format(s));
            }

            meta.setLore(formatLore);
        }
    }

    /**
     * Modify a item with a simple formatted name and lore as set
     * @param stack The old stack to modify
     * @param title The title. E.g: "&e&lFactionItem"
     * @param lore The lore the item should have
     * @return the old item tags with modified meta
     */
    public static ItemStack modiflyItem(ItemStack stack, String title, String... lore) {
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(Language.format(title));
        SetLore(lore, meta);
        ItemStack item = new ItemStack(stack);
        item.setItemMeta(meta);
        return item;
    }

    public static boolean mkdir(String path) {
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            return dirFile.mkdirs();
        }
        return true;
    }

    public static void run(ExceptionCallback cb) {
        cb.callback();
    }


    public static void except(Exception e) {
        if (MainIF.getConfigManager().getValue("general.printStacktrace")) e.printStackTrace();
        MainIF.getIF().SaveShutdown(e.getMessage());
    }

    public static String[] getNames(Class<? extends Enum<?>> e) {
        return Arrays.stream(e.getEnumConstants()).map(Enum::name).toArray(String[]::new);
    }

    public static String removeNonDigits(final String str) {
        if (str == null || str.length() == 0) {
            return "";
        }
        return str.replaceAll("/[^0-9]/g", "");
    }

    public static List<String> wrapLines(String text, String prefix) {
        String wrapped = WordUtils.wrap(text, MainIF.getConfigManager().getValue("gui.wrapLength"));
        return Arrays.stream(wrapped.split("\n")).map(x -> prefix + x).collect(Collectors.toList());
    }

    public static synchronized  <T extends FactionEventCancelledable> boolean callEvent(T event) {
        Bukkit.getScheduler().runTask(MainIF.getIF(), () -> Bukkit.getPluginManager().callEvent(event));

        return !event.isCancelled();
    }
}
