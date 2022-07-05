package io.github.toberocat.improvedfactions.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import java.util.Iterator;

public interface Gui {
    void onInventoryClick(InventoryClickEvent event, Iterator<Gui> iterator);
    void onInventoryDrag(InventoryDragEvent event, Iterator<Gui> iterator);
    void onInventoryClose(InventoryCloseEvent event, Iterator<Gui> iterator);
}
