package io.github.toberocat.improvedfactions.spigot.gui.provided;

import io.github.toberocat.improvedFactions.core.gui.JsonGui;
import io.github.toberocat.improvedfactions.spigot.gui.AbstractGui;
import io.github.toberocat.improvedfactions.spigot.gui.page.Page;
import io.github.toberocat.improvedfactions.spigot.gui.settings.GuiSettings;
import io.github.toberocat.improvedfactions.spigot.item.SpigotItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpigotEditJsonGui extends AbstractGui {

    private final List<String>[] slotActions;

    public SpigotEditJsonGui(@NotNull Player player, @NotNull JsonGui jsonGui) {
        super(player, createInv(player, jsonGui));
        slotActions = new List[inventory.getSize()];
        close = () -> {
            jsonGui.setContent(Arrays.stream(inventory.getContents())
                    .map(SpigotItemStack::new)
                    .toArray(SpigotItemStack[]::new));
        };
    }

    private static Inventory createInv(@NotNull Player player, @NotNull JsonGui jsonGui) {
        Inventory inventory = createInventory(player, 54, jsonGui.getGuiId());
        inventory.setContents(jsonGui.getContent()
                .keySet()
                .stream()
                .map(io.github.toberocat.improvedFactions.core.item.ItemStack::getRaw)
                .map(x -> (ItemStack) x)
                .toArray(ItemStack[]::new));

        return inventory;
    }

    @Override
    protected void addPage() {
        pages.add(new Page(inventory.getSize()));
    }

    @Override
    protected GuiSettings readSettings() {
        return new GuiSettings();
    }

    protected
}
