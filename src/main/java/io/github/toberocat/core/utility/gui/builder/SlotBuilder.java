package io.github.toberocat.core.utility.gui.builder;

import org.bukkit.inventory.ItemStack;

public class SlotBuilder {
    private int slot;
    private String clickEvent;
    private ItemStack stack;

    public SlotBuilder() {

    }
    public SlotBuilder(int slot, String clickEvent, ItemStack stack) {
        this.slot = slot;
        this.clickEvent = clickEvent;
        this.stack = stack;
    }

    public int getSlot() {
        return slot;
    }

    public SlotBuilder setSlot(int slot) {
        this.slot = slot;
        return this;
    }

    public String getClickEvent() {
        return clickEvent;
    }

    public SlotBuilder setClickEvent(String clickEvent) {
        this.clickEvent = clickEvent;
        return this;
    }

    public ItemStack getStack() {
        return stack;
    }

    public SlotBuilder setStack(ItemStack stack) {
        this.stack = stack;
        return this;
    }
}
