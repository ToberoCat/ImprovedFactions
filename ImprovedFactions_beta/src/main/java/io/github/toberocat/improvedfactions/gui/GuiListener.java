package io.github.toberocat.improvedfactions.gui;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

public class GuiListener implements Listener {

    public static List<Gui> guis = new ArrayList<>();

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        try {
            Iterator<Gui> it = guis.iterator();
            while (it.hasNext()) {
                Gui gui = it.next();
                gui.onInventoryClick(e, it);
            }
        } catch (ConcurrentModificationException ignored) {}
    }

    @EventHandler
    public void onInventoryDrag(final InventoryDragEvent e) {
        try {
            Iterator<Gui> it = guis.iterator();
            while (it.hasNext()) {
                Gui gui = it.next();
                gui.onInventoryDrag(e, it);
            }
        } catch (ConcurrentModificationException ignored) {}
    }

    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent e) {
        try {
            Iterator<Gui> it = guis.iterator();
            while (it.hasNext()) {
                Gui gui = it.next();
                gui.onInventoryClose(e, it);
            }
        } catch (ConcurrentModificationException ignored) {}
    }
}
