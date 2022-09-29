package io.github.toberocat.improvedfactions.spigot.gui.provided;

import io.github.toberocat.improvedFactions.core.gui.ItemContainer;
import io.github.toberocat.improvedFactions.core.gui.JsonGui;
import io.github.toberocat.improvedfactions.spigot.gui.AbstractGui;
import io.github.toberocat.improvedfactions.spigot.gui.page.Page;
import io.github.toberocat.improvedfactions.spigot.gui.settings.GuiSettings;
import io.github.toberocat.improvedfactions.spigot.item.SpigotItemStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SpigotEditJsonGui extends AbstractGui {

    private final String[] slotActions;

    public SpigotEditJsonGui(@NotNull Player player, @NotNull JsonGui jsonGui) {
        super(player, createInv(player, jsonGui));
        slotActions = new String[inventory.getSize()];

        org.bukkit.inventory.ItemStack[] content = new org.bukkit.inventory.ItemStack[inventory.getSize()];
        jsonGui.getContent().forEach((item, action) -> {
            slotActions[item.slot()] = action;
            content[item.slot()] = (org.bukkit.inventory.ItemStack) item.stack().getRaw();
        });
        inventory.setContents(content);

        close = () -> {
            io.github.toberocat.improvedFactions.core.item.ItemStack[] stacks = Arrays.stream(inventory.getContents())
                    .map(SpigotItemStack::new)
                    .toArray(SpigotItemStack[]::new);
            Map<ItemContainer, String> actionMap = new HashMap<>();
            for (int i = 0; i < stacks.length; i++) {
                io.github.toberocat.improvedFactions.core.item.ItemStack stack = stacks[i];
                if (stack == null) continue;

                String actions = slotActions[i];
                if (actions == null) continue;
                actionMap.put(new ItemContainer(i, stack), actions);
            }

            jsonGui.setContent(actionMap);
        };
    }

    private static Inventory createInv(@NotNull Player player, @NotNull JsonGui jsonGui) {
        return createInventory(player, 54, jsonGui.getGuiId());
    }

    @Override
    protected void addPage() {
        pages.add(new Page(inventory.getSize(), slot -> {
            //ToDo: Open Anvil GUI
        }));
    }

    @Override
    protected GuiSettings readSettings() {
        return new GuiSettings();
    }
}
