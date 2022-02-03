package io.github.toberocat.core.listeners;

import io.github.toberocat.core.utility.gui.Gui;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import java.util.*;

public class GuiListener implements Listener {

    public static List<Gui> guis = new ArrayList<>();

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        Iterator<Gui> it = guis.iterator();
        while (it.hasNext()) {
            Gui gui = it.next();
            gui.onInventoryClick(e, it);
        }
    }

    @EventHandler
    public void onInventoryDrag(final InventoryDragEvent e) {
        Iterator<Gui> it = guis.iterator();
        while (it.hasNext()) {
            Gui gui = it.next();
            gui.onInventoryDrag(e, it);
        }
    }

    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent e) {
        Iterator<Gui> it = guis.iterator();
        while (it.hasNext()) {
            Gui gui = it.next();
            gui.onInventoryClose(e, it);
        }
    }
}
