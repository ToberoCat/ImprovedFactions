package io.github.toberocat.core.utility.gui.slot;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class Slot {
    protected ItemStack stack;

    public Slot(ItemStack stack) {
        this.stack = stack;
    }

    public abstract void click(Player player);

    public ItemStack getStack() {
        return stack;
    }

    public void setStack(ItemStack stack) {
        this.stack = stack;
    }
}
