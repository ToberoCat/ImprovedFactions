package io.github.toberocat.improvedfactions.spigot.gui;

import io.github.toberocat.improvedfactions.spigot.gui.page.AutoNavPage;
import io.github.toberocat.improvedfactions.spigot.gui.page.AutoPage;
import io.github.toberocat.improvedfactions.spigot.gui.page.Page;
import io.github.toberocat.improvedfactions.spigot.gui.slot.EnumSlot;
import io.github.toberocat.improvedfactions.spigot.gui.slot.Slot;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class AutoGui extends AbstractGui {
    protected final int[] usableSlots;

    public AutoGui(@NotNull Player player, @NotNull Inventory inventory, int[] usableSlots) {
        super(player, inventory);
        this.usableSlots = usableSlots;

        addPage();
    }


    @Override
    protected void addPage() {
        if (usableSlots == null) return;
        pages.add(new AutoNavPage(inventory.getSize(), usableSlots));
    }

    public void addSlot(Slot slot) {
        if (slot == null) return;
        Page page = pages.get(pages.size() - 1);

        boolean execute = false;
        if (page instanceof AutoPage auto) execute = auto.addSlot(slot);
        else if (page instanceof AutoNavPage navPage) execute = navPage.addSlot(slot);
        if (execute) addPage();
    }

    public <T extends Enum<T>> void addEnumSlot(ItemStack stack, Class<T> enumType, Consumer<String> updateSelector) {
        Page page = pages.get(pages.size() - 1);
        EnumSlot<T> slot = new EnumSlot<>(stack, enumType, 0, this::render) {
            @Override
            public void changeSelected(String newValue) {
                updateSelector.accept(newValue);
            }
        };


        boolean execute = false;
        if (page instanceof AutoPage auto) execute = auto.addSlot(slot);
        else if (page instanceof AutoNavPage navPage) execute = navPage.addSlot(slot);

        if (execute) addPage();
    }

    public void addSlot(ItemStack stack, BiConsumer<Player, ItemStack> click) {
        addSlot(new Slot(stack) {
            @Override
            public void click(@NotNull Player player, @Nullable ItemStack cursor) {
                click.accept(player, cursor);
            }

        });
    }

    public void addSlot(ItemStack stack, Consumer<Player> click) {
        addSlot(stack, (p, c) -> click.accept(p));
    }
}
