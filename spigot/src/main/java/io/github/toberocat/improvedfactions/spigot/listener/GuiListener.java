package io.github.toberocat.improvedfactions.spigot.listener;

import io.github.toberocat.improvedfactions.spigot.MainIF;
import io.github.toberocat.improvedfactions.spigot.gui.AbstractGui;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class GuiListener extends SpigotEventListener {

    public static final ArrayList<AbstractGui> GUIS = new ArrayList<>();
    private static final HashMap<UUID, Integer> currentTests = new HashMap<>();

    public GuiListener(@NotNull MainIF plugin) {
        super(plugin);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (GUIS.stream().noneMatch(x -> x.getInventory() == e.getClickedInventory())) return;

        for (AbstractGui gui : new ArrayList<>(GUIS)) gui.click(e);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
        for (AbstractGui gui : new ArrayList<>(GUIS)) gui.drag(e);

    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        for (AbstractGui gui : new ArrayList<>(GUIS)) gui.close(e);

    }
}
