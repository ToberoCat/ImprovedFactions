package io.github.toberocat.improvedfactions.spigot.gui.slot;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

public abstract class BoolSlot extends EnumSlot<BoolSlot.BoolSelector> {

    public BoolSlot(ItemStack stack, boolean def, Runnable rerender) {
        super(stack, BoolSelector.class, def ? 1 : 0, rerender);
    }

    @Override
    public void changeSelected(String newValue) {
        change(newValue.equals("§7True"));
    }

    public abstract void change(boolean value);

    protected enum BoolSelector {
        FALSE,
        TRUE;

        @Override
        public String toString() {
            return "§7" + (name().equals("TRUE") ? "True" : "False");
        }
    }
}
