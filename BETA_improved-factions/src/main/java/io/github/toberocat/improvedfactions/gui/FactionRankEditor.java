package io.github.toberocat.improvedfactions.gui;

import io.github.toberocat.improvedfactions.factions.Faction;
import io.github.toberocat.improvedfactions.factions.FactionRankPermission;
import io.github.toberocat.improvedfactions.language.Language;
import io.github.toberocat.improvedfactions.ranks.Rank;
import io.github.toberocat.improvedfactions.utility.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FactionRankEditor implements Gui {
    private Inventory inv;

    private FactionRankPermission permissions;

    private int page;
    private int maxPage;
    private int lastSlotIndex;
    public final static Integer[] flagSlots = new Integer[] {
            1, 10, 19, 28, 37,
            4, 13, 22, 31, 40,
            7, 16, 25, 34, 43
    };

    //key = page; value
    private Map<Integer, Map<Integer, Rank>> flags = new HashMap<>();

    private Faction faction;

    public FactionRankEditor(Player player, String title, Faction faction, FactionRankPermission permissions) {
        GuiListener.guis.add(this);
        this.permissions = permissions;
        this.faction = faction;
        page = 0;
        maxPage = 0;

        for (Rank rank : Rank.ranks) {
            AddPermissionFlag(rank);
        }

        openInventory(player, title);
    }

    private void AddPermissionFlag(Rank rank) {
        AddToSlot(rank);
    }

    private void openInventory(Player player, String title) {
        inv = Bukkit.createInventory(null, 54, Language.format("&a&l"+title));
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
            inv.setItem(45, Utils.createItem(Material.ARROW, "§c§lGo back", new String[] {
                    "permissions"
            }));
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
            Rank rank = GetSlot(key);
            inv.setItem(key, Utils.modiflyItem(rank.getItem(),
                    (permissions.getRanks().contains(rank) ? "§a" : "§c") + ChatColor.stripColor(rank.getDisplayName()), rank.getDescription()));
        }
    }

    private void AddToSlot(Rank rank) {
        int slot = flagSlots[lastSlotIndex++];
        if (lastSlotIndex >= flagSlots.length) {
            lastSlotIndex = 0;
            maxPage++;
        }
        if (flags.containsKey(maxPage)) {
            flags.get(maxPage).put(slot, rank);
        } else {
            Map<Integer, Rank> map = new HashMap<>();
            map.put(slot, rank);
            flags.put(maxPage, map);
        }
    }

    private Rank GetSlot(int slot) {
        if (flags.containsKey(page)) {
            return flags.get(page).get(slot);
        }
        return null;
    }
    private Rank GetSlot(int page, int slot) {
        if (flags.containsKey(page)) {
            return flags.get(page).get(slot);
        }
        return null;
    }

    private ItemStack SetPermission(int slot, Rank rank) {
        if (permissions.getRanks().contains(rank)) {
            permissions.getRanks().remove(rank);
            return Utils.modiflyItem(rank.getItem(), "§c" + ChatColor.stripColor(rank.getDisplayName()));
        } else {
            permissions.getRanks().add(rank);
            return Utils.modiflyItem(rank.getItem(), "§a" + ChatColor.stripColor(rank.getDisplayName()));
        }
    }

    @Override
    public void onInventoryClick(InventoryClickEvent e, Iterator<Gui> iterator) {
        if (e.getInventory() != inv) return;

        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();

        if (clickedItem == null || clickedItem.getType().isAir()) return;

        final Player p = (Player) e.getWhoClicked();

        Rank rank = GetSlot(e.getRawSlot());
        if (rank != null)
            inv.setItem(e.getRawSlot(), SetPermission(e.getRawSlot(), rank));
        else if (clickedItem.getType() == Material.ARROW) {
            if (clickedItem.getItemMeta().getLore().get(0).contains("permissions")) {
                new FactionRanksGui(p, faction);
            } else {
                page = Integer.parseInt(ChatColor.stripColor(clickedItem.getItemMeta().getLore().get(0)));
                CreatePage();
            }
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
