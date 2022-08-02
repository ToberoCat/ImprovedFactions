package io.github.toberocat.factions.core.rank;

import io.github.toberocat.factions.core.wrapper.inventory.ItemWrapper;

public abstract class GuestRank extends Rank {
    public static final String register = "Guest";

    public GuestRank(String displayName, ItemWrapper icon) {
        super(displayName, register, permissionPriority, isAdmin);
    }
}
