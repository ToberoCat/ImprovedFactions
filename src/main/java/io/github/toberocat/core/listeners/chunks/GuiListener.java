package io.github.toberocat.core.listeners.chunks;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.gui.AbstractGui;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import java.util.*;

public class GuiListener implements Listener {

    public static final List<AbstractGui> GUIS = new ArrayList<>();
    private static final Map<UUID, Integer> currentTests = new HashMap<>();
    private static int taskId = -1;
    private final int maxCps = MainIF.getConfigManager().getValue("gui.maxCps");
    private final int closeGuiCps = MainIF.getConfigManager().getValue("gui.closeGuiCps");

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (GUIS.stream().noneMatch(x -> x.getInventory() == e.getClickedInventory())) return;

        Iterator<AbstractGui> it = GUIS.iterator();
        if (updateCps(e.getWhoClicked().getUniqueId()) >= maxCps) {
            e.setCancelled(true);
            if (currentTests.get(e.getWhoClicked().getUniqueId()) >= closeGuiCps) e.getWhoClicked().closeInventory();
            return;
        }


        while (it.hasNext()) {
            AbstractGui gui = it.next();
            gui.click(e);
        }
    }

    @EventHandler
    public void onInventoryDrag(final InventoryDragEvent e) {
        for (AbstractGui gui : GUIS) {
            gui.drag(e);
        }
    }

    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent e) {
        for (AbstractGui gui : GUIS) {
            gui.close(e);
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
