package io.github.toberocat.improvedfactions.spigot.gui;

import io.github.toberocat.improvedfactions.spigot.gui.settings.GuiSettings;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class TabbedGui extends AutoGui {
    public static final int[] USABLE_SLOTS_5 = {
            1, 10, 19, 28, 37, // First column
            4, 13, 22, 31, 40, // Second column
            7, 16, 25, 34, 43  // Last column
    };

    public static final int[] USABLE_SLOTS_4 = {
            1, 10, 19, 28,
            4, 13, 22, 31,
            7, 16, 25, 34
    };

    public static final int[] USABLE_SLOTS_3 = {
            1, 10, 19,
            4, 13, 22,
            7, 16, 25
    };

    public static final int[] USABLE_SLOTS_2 = {
            1, 10,
            4, 13,
            7, 16
    };

    public static final int[] USABLE_SLOTS_1 = {
            1,
            4,
            7
    };

    public TabbedGui(@NotNull Player player, @NotNull Inventory inventory) {
        super(player, inventory, getUsableSlots(inventory.getSize()));
    }

    private static int[] getUsableSlots(int size) {
        return switch (size / 9 - 1) {
            case 5 -> USABLE_SLOTS_5;
            case 4 -> USABLE_SLOTS_4;
            case 3 -> USABLE_SLOTS_3;
            case 2 -> USABLE_SLOTS_2;
            case 1 -> USABLE_SLOTS_1;
            default -> new int[0];
        };
    }

    @Override
    protected GuiSettings readSettings() {
        return new GuiSettings().setPageArrows(true);
    }
}
