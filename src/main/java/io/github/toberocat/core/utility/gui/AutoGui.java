package io.github.toberocat.core.utility.gui;

import io.github.toberocat.core.utility.gui.page.AutoPage;
import io.github.toberocat.core.utility.gui.slot.EnumSlot;
import io.github.toberocat.core.utility.gui.slot.Slot;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public abstract class AutoGui extends AbstractGui {
    private final int[] usableSlots;

    public AutoGui(@NotNull Player player, @NotNull Inventory inventory, int[] usableSlots) {
        super(player, inventory);
        this.usableSlots = usableSlots;

        addPage();
    }


    @Override
    protected void addPage() {
        if (usableSlots == null) return;
        pages.add(new AutoPage(inventory.getSize(), usableSlots));
    }

    public void addSlot(Slot slot) {
        if (slot == null) return;
        if (((AutoPage) pages.get(pages.size() - 1)).addSlot(slot)) addPage();
    }

    public <T extends Enum<T>> void addEnumSlot(ItemStack stack, Class<T> enumType, Consumer<String> updateSelector) {
        if (((AutoPage) pages.get(pages.size() - 1)).addSlot(new EnumSlot<>(stack, enumType, this::render) {
            @Override
            public void changeSelected(String newValue) {
                updateSelector.accept(newValue);
            }
        })) addPage();
    }

    public void addSlot(ItemStack stack, Consumer<Player> click) {
        if (((AutoPage) pages.get(pages.size() - 1)).addSlot(new Slot(stack) {
            @Override
            public void click(Player player) {
                click.accept(player);
            }

        })) addPage();
    }
}
