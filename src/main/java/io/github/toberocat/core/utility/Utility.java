package io.github.toberocat.core.utility;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.async.AsyncTask;
import io.github.toberocat.core.utility.callbacks.Callback;
import io.github.toberocat.core.utility.callbacks.ExceptionCallback;
import io.github.toberocat.core.utility.events.faction.FactionEvent;
import io.github.toberocat.core.utility.events.faction.FactionEventCancelledable;
import io.github.toberocat.core.utility.gitreport.GitReport;
import io.github.toberocat.core.utility.language.Language;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class Utility {

    @SuppressWarnings("unused")
    public static boolean isNumber(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String printStackToString(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    /**
     * Removed the first element in list and returns it
     *
     * @param list the list where the item should be removed
     * @param <T>  Type of the list
     * @return A objectPair. First object in there is the shifted element and them second is the new list
     */
    public static <T> ObjectPair<T, T[]> shift(T[] list) {
        List<T> t = Arrays.asList(list);
        MainIF.logMessage(Level.INFO, "" + list.length);
        T tShift = t.remove(0);
        return new ObjectPair<>(tShift, toArray(t));
    }

    /**
     * Make a generic list to an array
     *
     * @param list for convertion
     * @param <T>  Type of the list
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
     *
     * @param material The material for this item
     * @param name     The title. E.g: "&e&lFactionItem"
     * @return itemstack with a custom title and your set material
     */
    public static ItemStack createItem(final Material material, final String name) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(Language.format(name));

        item.setItemMeta(meta);

        return item;
    }

    public static List<String> getLore(ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();
        if (meta == null || meta.getLore() == null) return new ArrayList<>();
        return meta.getLore();
    }

    /**
     * Create a item with a simple formatted name and lore as array
     *
     * @param material The material for this item
     * @param name     The title. E.g: "&e&lFactionItem"
     * @param lore     The lore the item should have
     * @return itemstack with a custom title, custom lore and your set material
     */
    public static ItemStack createItem(final Material material, final String name, final String[] lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(Language.format(name));

        meta.setLore(setLore(item, lore).getItemMeta().getLore());

        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack setLore(ItemStack stack, String[] lore) {
        ItemStack newStack = new ItemStack(stack);
        ItemMeta meta = newStack.getItemMeta();
        assert meta != null;
        meta.setLore(Arrays.stream(lore).map(Language::format).toList());
        newStack.setItemMeta(meta);
        return newStack;
    }

    /**
     * Modify an item with a simple formatted name and lore as set
     *
     * @param stack The old stack to modify
     * @param title The title. E.g: "&e&lFactionItem"
     * @param lore  The lore the item should have
     * @return the old item tags with modified meta
     */
    public static ItemStack modiflyItem(ItemStack stack, String title, String... lore) {
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(Language.format(title));
        meta.setLore(setLore(stack, lore).getItemMeta().getLore());
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
        cb.run();
    }

    public static void except(Exception e) {
        if (Boolean.TRUE.equals(MainIF.getConfigManager().getValue("general.printStacktrace")))
            e.printStackTrace();
        MainIF.getIF().saveShutdown(e.getMessage());

        if (MainIF.getConfigManager().getValue("general.sendCrashesToGithub")) {
            MainIF.logMessage(Level.INFO, "Please wait while the crash gets reported to the developer. Don't restart or shutdown the server");
            AsyncTask.run(() -> GitReport.reportIssue(e)).then((result) -> MainIF.logMessage(Level.INFO,
                    "Reported the issue"));
        }
    }

    public static <T> String[] getNames(T enumForNames) {
        return Arrays.stream(enumForNames.getClass().getEnumConstants()).map(Object::toString).toArray(String[]::new);
    }

    public static String removeNonDigits(String str) {
        if (str == null || str.length() == 0) {
            return "0";
        }
        str = str.replaceAll("[^0-9]", "");
        return str;

    }

    public static List<String> wrapLines(String text, String prefix) {
        String wrapped = WordUtils.wrap(text, MainIF.getConfigManager().getValue("gui.wrapLength"));
        return Arrays.stream(wrapped.split("\n")).map(x -> prefix + x).collect(Collectors.toList());
    }

    public static synchronized <T extends FactionEventCancelledable> boolean callEvent(T event) {
        Bukkit.getScheduler().runTask(MainIF.getIF(), () -> Bukkit.getPluginManager().callEvent(event));

        return !event.isCancelled();
    }

    public static synchronized <T extends FactionEvent> void callEvent(T event) {
        Bukkit.getScheduler().runTask(MainIF.getIF(), () -> Bukkit.getPluginManager().callEvent(event));
    }

    public static String getTime(long timing) {
        long time = System.currentTimeMillis() - timing;
        time /= 1000;
        int secs = (int) (time % 60);
        time /= 60;
        int mins = (int) (time % 60);
        time /= 60;
        int hours = (int) (time % 24);
        time /= 24;
        int days = (int) time;

        return (days < 10 ? "0" + days : days) + "d " +
                (hours < 10 ? "0" + hours : hours) + "h " +
                (mins < 10 ? "0" + mins : mins) + "m " +
                (secs < 10 ? "0" + secs : secs) + "s";
    }

    public static ItemStack getSkull(OfflinePlayer player, int count, String name, String[] lore) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, count, (short) 3);
        SkullMeta skull = (SkullMeta) item.getItemMeta();
        skull.setDisplayName(name);
        skull.setLore(Arrays.asList(lore));
        skull.setOwningPlayer(player);
        item.setItemMeta(skull);
        return item;
    }

    public static ItemStack getSkull(String url, int count, String name, String... lore) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, count);
        if (url.isEmpty()) return head;

        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField = null;
        try {
            assert headMeta != null;
            profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1) {
            e1.printStackTrace();
        }
        head.setItemMeta(headMeta);

        ItemMeta meta = head.getItemMeta();
        assert meta != null;
        meta.setDisplayName(name);
        if (lore != null) meta.setLore(Arrays.asList(lore));
        head.setItemMeta(meta);
        return head;
    }

    public static void run(ExceptionCallback cb, Callback except) {
        try {
            cb.ECallback();
        } catch (Exception e) {
            except(e);
            except.callback();
        }

    }

    public static float lerp(float point1, float point2, float alpha) {
        return point1 + alpha * (point2 - point1);
    }

    public static double lerp(double point1, double point2, double alpha) {
        return point1 + alpha * (point2 - point1);
    }

    public static float clamp(float value, float min, float max) {
        return Math.max(Math.min(value, max), min);
    }

    public static double clamp(double value, double min, double max) {
        return Math.max(Math.min(value, max), min);
    }

}
