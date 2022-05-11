package io.github.toberocat.core.listeners;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.gui.Gui;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import java.util.*;

public class GuiListener implements Listener {

    public static final List<Gui> GUIS = new ArrayList<>();
    private static final Map<UUID, Integer> currentTests = new HashMap<>();
    private static int taskId = -1;
    private final int maxCps = MainIF.getConfigManager().getValue("gui.maxCps");
    private final int closeGuiCps = MainIF.getConfigManager().getValue("gui.closeGuiCps");

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (GUIS.stream().noneMatch(x -> x.getInventory() == e.getClickedInventory())) return;

        Iterator<Gui> it = GUIS.iterator();
        if (updateCps(e.getWhoClicked().getUniqueId()) >= maxCps) {
            e.setCancelled(true);
            if (currentTests.get(e.getWhoClicked().getUniqueId()) >= closeGuiCps) e.getWhoClicked().closeInventory();
            return;
        }


        while (it.hasNext()) {
            Gui gui = it.next();
            gui.onInventoryClick(e, it);
        }
    }

    @EventHandler
    public void onInventoryDrag(final InventoryDragEvent e) {
        Iterator<Gui> it = GUIS.iterator();
        while (it.hasNext()) {
            Gui gui = it.next();
            gui.onInventoryDrag(e, it);
        }
    }

    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent e) {
        Iterator<Gui> it = GUIS.iterator();
        while (it.hasNext()) {
            Gui gui = it.next();
            gui.onInventoryClose(e, it);
        }
    }

    private void startTask() {
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(MainIF.getIF(), () -> {
            if (currentTests.isEmpty()) {
                Bukkit.getScheduler().cancelTask(taskId);
                taskId = -1;
                return;
            }
            currentTests.clear();
        }, 0, 20);
    }


    private int updateCps(UUID uuid) {
        if (taskId == -1) startTask();

        if (!currentTests.containsKey(uuid)) currentTests.put(uuid, 0);

        int count = currentTests.get(uuid) + 1;

        currentTests.put(uuid, count);

        return count;
    }
}
