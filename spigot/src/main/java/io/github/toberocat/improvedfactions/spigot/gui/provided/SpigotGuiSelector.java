package io.github.toberocat.improvedfactions.spigot.gui.provided;

import io.github.toberocat.improvedFactions.core.gui.GuiManager;
import io.github.toberocat.improvedFactions.core.gui.JsonGui;
import io.github.toberocat.improvedFactions.core.handler.ColorHandler;
import io.github.toberocat.improvedfactions.spigot.MainIF;
import io.github.toberocat.improvedfactions.spigot.gui.TabbedGui;
import io.github.toberocat.improvedfactions.spigot.gui.slot.Slot;
import io.github.toberocat.improvedfactions.spigot.utils.ItemUtils;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpigotGuiSelector extends TabbedGui {
    public SpigotGuiSelector(@NotNull Player player) {
        super(player, createInventory(player, 54, "Select a gui"));
        GuiManager.getGuis().forEach(this::addEditor);

        render();
    }

    private void addEditor(@NotNull String guiId) {
        JsonGui gui = GuiManager.getGui(guiId);
        addSlot(new Slot(ItemUtils.createItem(Material.BOOK, 1, gui.getTitle(), "",
                "§7Customize this gui to fit your server",
                "",
                "§7§nEditor:",
                "§7When you want to an item, use §e/f gui translate§7",
                "§7first, so the item gets a name, that will load",
                "§7it's actual lore and title from the corresponding lang file",
                "§7Each item has a action that's called like the translatable in",
                "§7the §eactions/" + guiId + "/§7 folder",
                "",
                "§7§nControls:",
                "§7Left click: §eEdit content",
                "§7Right click: §eChange title",
                "§7Shift Right click: §eChange rows")) {
            @Override
            public void leftClick(@NotNull Player player, @Nullable ItemStack cursor) {
                JsonGui gui = GuiManager.getGui(guiId);
                new SpigotEditJsonGui(player, gui);
            }

            @Override
            public void rightClick(@NotNull Player player, @Nullable ItemStack cursor) {
                new AnvilGUI.Builder()
                        .onComplete((u, text) -> {
                            gui.setTitle(ColorHandler.api().format(text));
                            gui.write();
                            new SpigotGuiSelector(u);
                            return AnvilGUI.Response.close();
                        })
                        .text(gui.getTitle().replaceAll("§", "&"))
                        .itemLeft(ItemUtils.createItem(Material.BOOK, gui.getTitle()))
                        .plugin(MainIF.getPlugin(MainIF.class))
                        .open(player);
            }

            @Override
            public void shiftRightClick(@NotNull Player player, @Nullable ItemStack cursor) {
                new AnvilGUI.Builder()
                        .onComplete((u, text) -> {
                            try {
                                gui.setRows(Integer.parseInt(text));
                                gui.write();
                                new SpigotGuiSelector(u);
                                return AnvilGUI.Response.close();
                            } catch (NumberFormatException e) {
                                return AnvilGUI.Response.text("No number");
                            }
                        })
                        .text(String.valueOf(gui.getRows()))
                        .itemLeft(ItemUtils.createItem(Material.BOOK, gui.getTitle()))
                        .plugin(MainIF.getPlugin(MainIF.class))
                        .open(player);
            }
        });
    }
}
