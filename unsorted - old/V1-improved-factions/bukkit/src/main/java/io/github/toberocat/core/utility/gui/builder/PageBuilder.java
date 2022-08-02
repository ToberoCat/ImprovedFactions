package io.github.toberocat.core.utility.gui.builder;

import io.github.toberocat.core.utility.gui.slot.Slot;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashSet;

public class PageBuilder {
    private LinkedHashSet<SlotBuilder> slots = new LinkedHashSet<>();

    public PageBuilder add(Slot slot, int invSlot) {
        SlotBuilder builder = new SlotBuilder();
        builder.setSlot(invSlot);
        builder.setStack(slot.getStack());
        builder.setClickEvent(slot.getStack().getType().name() + "::" + invSlot);

        slots.add(builder);

        return this;
    }

    public PageBuilder add(ItemStack stack, int invSlot) {
        SlotBuilder builder = new SlotBuilder();
        builder.setSlot(invSlot);
        builder.setStack(stack);
        builder.setClickEvent(stack.getType().name() + "::" + invSlot);

        slots.add(builder);

        return this;
    }

    public PageBuilder add(ItemStack stack, int invSlot, String clickEvent) {
        SlotBuilder builder = new SlotBuilder();
        builder.setSlot(invSlot);
        builder.setStack(stack);
        builder.setClickEvent(stack.getType().name() + "::" + invSlot);

        slots.add(builder);

        return this;
    }

    public PageBuilder add(Slot slot) {
        SlotBuilder builder = new SlotBuilder();
        builder.setSlot(-1);
        builder.setStack(slot.getStack());
        builder.setClickEvent(slot.getStack().getType().name() + "::-1");

        slots.add(builder);

        return this;
    }

    public LinkedHashSet<SlotBuilder> getSlots() {
        return slots;
    }

    public PageBuilder setSlots(LinkedHashSet<SlotBuilder> slots) {
        this.slots = slots;
        return this;
    }
}
