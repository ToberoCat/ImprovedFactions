package io.github.toberocat.improvedfactions.spigot.gui.page;

import io.github.toberocat.improvedfactions.spigot.gui.slot.Slot;

public class AutoNavPage extends NavigationPage {
    private final int[] freeSlots;
    protected int lastFree;

    public AutoNavPage(int inventorySize, int[] freeSlots) {
        super(inventorySize);
        this.freeSlots = freeSlots;
    }

    @Override
    public void clear() {
        super.clear();
        lastFree = 0;
    }

    public boolean addSlot(Slot slot) {
        int invSlot = freeSlots[lastFree];
        slots[invSlot] = slot;
        lastFree++;

        return lastFree >= freeSlots.length;
    }

}
