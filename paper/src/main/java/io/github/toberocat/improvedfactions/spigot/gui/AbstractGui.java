package io.github.toberocat.improvedfactions.spigot.gui;

import io.github.toberocat.improvedFactions.core.handler.message.MessageHandler;
import io.github.toberocat.improvedfactions.spigot.gui.page.Page;
import io.github.toberocat.improvedfactions.spigot.gui.settings.GuiSettings;
import io.github.toberocat.improvedfactions.spigot.gui.slot.Slot;
import io.github.toberocat.improvedfactions.spigot.listener.GuiListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static io.github.toberocat.improvedfactions.spigot.utils.ItemUtils.createItem;

public abstract class AbstractGui {

    protected final GuiSettings settings;
    protected final Player player;
    protected Inventory inventory;
    protected int currentPage;
    protected Runnable close;
    protected List<Page> pages;

    public AbstractGui(@NotNull Player player, @NotNull Inventory inventory) {
        GuiListener.GUIS.add(this);

        this.inventory = inventory;
        pages = new ArrayList<>();
        currentPage = 0;
        settings = readSettings();
        this.player = player;

        player.openInventory(inventory);
        addPage();
    }

    /* Static utility function */
    public static Inventory createInventory(Player player, int size, String title) {
        return Bukkit.createInventory(player, size, MessageHandler.api().format(title).strip());
    }

    /* Abstract methods */

    protected abstract void addPage();

    protected abstract GuiSettings readSettings();


    /* Event methods */

    public void click(InventoryClickEvent event) {
        event.setCancelled(true);
        int lastCurrentPage = currentPage;

        if (settings.isPageArrows()) {
            ItemStack clicked = event.getCurrentItem();
            if (clicked == null) return;

            if (event.getSlot() == inventory.getSize() - 9 &&
                    clicked.getType() == Material.ARROW) currentPage--;

            if (event.getSlot() == inventory.getSize() - 1 &&
                    clicked.getType() == Material.ARROW) currentPage++;
        }

        if (settings.getQuitGui() != null && event.getSlot() == inventory.getSize() - 5) {
            settings.getQuitGui().run();
            return;
        }

        currentPage += pages.get(currentPage).click(event, currentPage);
        currentPage = clamp(currentPage, 0, pages.size());


        if (lastCurrentPage != currentPage) render();
    }

    public void drag(InventoryDragEvent event) {
        event.setCancelled(true);
    }

    public final void close(@Nullable InventoryCloseEvent event) {
        if (event == null) return;

        if (event.getInventory().equals(inventory)) {
            GuiListener.GUIS.remove(this);
            if (close != null) close.run();
        }
    }


    /* Inventory Management */

    public void render() {
        pages.get(currentPage).render(inventory);
        if (settings.isPageArrows()) renderArrows(inventory, currentPage, pages.size() - 1);
        if (settings.getQuitGui() != null) inventory.setItem(inventory.getSize() - 5,
                createItem(Material.BARRIER, "&cExit"));
    }

    protected void renderArrows(Inventory inventory, int current, int max) {
        if (current != 0) inventory.setItem(inventory.getSize() - 9,
                createItem(Material.ARROW, "&c&lBack"));
        if (max != current) inventory.setItem(inventory.getSize() - 1,
                createItem(Material.ARROW, "&a&lNext"));
    }

    public void clear() {
        pages.get(currentPage).clear();
        pages.get(currentPage).render(inventory);
    }

    public void addSlot(Slot slot, int page, int invSlot) {
        if (pages.get(page).addSlot(slot, invSlot)) addPage();
    }

    public void addSlot(ItemStack stack, int page, int invSlot, BiConsumer<Player, ItemStack> click) {
        if (pages.get(page).addSlot(new Slot(stack) {
            @Override
            public void leftClick(@NotNull Player player, @Nullable ItemStack cursor) {
                click.accept(player, cursor);
            }
        }, invSlot)) addPage();
    }

    public void addSlot(ItemStack stack, int page, int invSlot, Consumer<Player> click) {
        addSlot(stack, page, invSlot, (p, c) -> click.accept(p));
    }

    public void changePage(int amount) {
        currentPage = clamp(currentPage + amount, 0, pages.size());
        render();
    }

    /* Getter and Setter */

    public void closeEvent(Runnable close) {
        this.close = close;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    protected static int clamp(int value, int min, int max) {
        return Math.max(Math.min(value, max), min);
    }

}
