package io.github.toberocat.improvedfactions.gui;

import io.github.toberocat.improvedfactions.ImprovedFactionsMain;
import io.github.toberocat.improvedfactions.factions.Faction;
import io.github.toberocat.improvedfactions.language.Language;
import io.github.toberocat.improvedfactions.utility.Utils;
import io.github.toberocat.improvedfactions.utility.Callback;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class FactionSettingsGui implements Gui {
    private Inventory inv;

    private Faction faction;

    private int page;
    private int maxPage;
    private int lastSlotIndex;
    public final static Integer[] flagSlots = new Integer[] {
            1, 10, 19, 28, 37,
            4, 13, 22, 31, 40,
            7, 16, 25, 34, 43
    };

    //key = page; value
    private Map<Integer, Map<Integer, Flag>> flags = new HashMap<>();

    public FactionSettingsGui(Player player, Faction faction) {
        GuiListener.guis.add(this);
        this.faction = faction;
        page = 0;
        maxPage = 0;
        FileConfiguration config = ImprovedFactionsMain.getPlugin().getConfig();
        for (String key : faction.getSettings().getFlags().keySet()) {
            Flag flag = faction.getSettings().getFlags().get(key);
            if (config.getBoolean("factions.flags." + key)) {
                AddToSlot(flag);
            }
        }

        openInventory(player, faction);
    }

    private <T extends Enum<T>> void AddEnumFlag(Class<T> aEnum, T selected, Material material, String flagName) {
        List<T> values = java.util.Arrays.asList(aEnum.getEnumConstants());
        if (values.size() != 0) {
            AddToSlot(new Flag<>(Flag.FlagType.Enum, material, flagName, values, selected.ordinal()));
        }
    }

    private void AddBooleanFlag(boolean value, Material material, String flagName) {

        AddToSlot(new Flag<>(Flag.FlagType.Boolean, material, flagName, value));
    }

    private void AddFunctionFlag(Material material, String flagName, String description, Callback callback) {
        AddToSlot(new Flag<>(Flag.FlagType.Function, material, flagName, description, callback));
    }

    private void openInventory(Player player, Faction faction) {
        inv = Bukkit.createInventory(null, 54, Language.format(faction.getDisplayName() + " &a&lSettings"));
        inv.clear();

        ItemStack[] contents = inv.getContents();
        Arrays.fill(contents, new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE));
        inv.setContents(contents);

        CreatePage();

        player.openInventory(inv);
    }

    private void CreatePage() {
        //fill last row
        if (page != 0)
            inv.setItem(45, Utils.createItem(Material.ARROW, "§c§lGo back", new String[] {
                    (page - 1) + ""
            }));
        else
            inv.setItem(45, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        inv.setItem(46, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        inv.setItem(47, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        inv.setItem(48, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        inv.setItem(49, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        inv.setItem(50, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        inv.setItem(51, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        inv.setItem(52, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        if (page != maxPage)
            inv.setItem(53, Utils.createItem(Material.ARROW, "§a§lNext page", new String[] {
                    (page + 1) + ""
            }));
        else
            inv.setItem(53, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));

        for (Integer key : flags.get(page).keySet()) {
            inv.setItem(key, GetSlot(key).getItem());
        }
    }

    private synchronized void AddToSlot(Flag flag) {
        int slot = flagSlots[lastSlotIndex+1];
        if (lastSlotIndex >= flagSlots.length) {
            lastSlotIndex = 0;
            maxPage++;
        }
        if (flags.containsKey(maxPage)) {
            flags.get(maxPage).put(slot, flag);
        } else {
            Map<Integer, Flag> map = new HashMap<>();
            map.put(slot, flag);
            flags.put(maxPage, map);
        }
        lastSlotIndex += 1;
    }

    private Flag GetSlot(int slot) {
        if (flags.containsKey(page)) {
            return flags.get(page).get(slot);
        }
        return null;
    }
    private Flag GetSlot(int page, int slot) {
        if (flags.containsKey(page)) {
            return flags.get(page).get(slot);
        }
        return null;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent e, Iterator<Gui> iterator) {
        if (e.getInventory() != inv) return;

        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();

        if (clickedItem == null || clickedItem.getType().isAir()) return;

        final Player p = (Player) e.getWhoClicked();

        Flag flag = GetSlot(e.getRawSlot());
        if (flag != null)
            inv.setItem(e.getRawSlot(), flag.ToggleSetting(faction, p));
        else if (clickedItem.getType() == Material.ARROW) {
            page = Integer.parseInt(ChatColor.stripColor(clickedItem.getItemMeta().getLore().get(0)));
            CreatePage();
        }

    }

    @Override
    public void onInventoryDrag(InventoryDragEvent e, Iterator<Gui> iterator) {
        if (e.getInventory().equals(inv)) {
            e.setCancelled(true);
        }
    }

    @Override
    public void onInventoryClose(InventoryCloseEvent e, Iterator<Gui> iterator) {
        if (e.getInventory().equals(inv)) {
            iterator.remove();
        }
    }

}
