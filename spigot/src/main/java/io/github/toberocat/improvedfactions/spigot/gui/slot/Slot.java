package io.github.toberocat.improvedfactions.spigot.gui.slot;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Slot {
    protected ItemStack stack;

    public Slot(ItemStack stack) {
        this.stack = stack;
    }

    public abstract void click(@NotNull Player player, @Nullable ItemStack cursor);

    public void rightClick(@NotNull Player player, @Nullable ItemStack cursor) {
    }

    public ItemStack getStack() {
        return stack;
    }

    public void setStack(ItemStack stack) {
        this.stack = stack;
    }
}
