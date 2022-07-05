package io.github.toberocat.core.utility.gui.page;

import io.github.toberocat.core.utility.gui.slot.Slot;

public class AutoPage extends NavigationPage {
    private final int[] freeSlots;
    protected int lastFree;

    public AutoPage(int inventorySize, int[] freeSlots) {
        super(inventorySize);
        this.freeSlots = freeSlots;
    }

    public boolean addSlot(Slot slot) {
        int invSlot = freeSlots[lastFree];
        slots[invSlot] = slot;
        lastFree++;

        return lastFree >= freeSlots.length;
    }

}
