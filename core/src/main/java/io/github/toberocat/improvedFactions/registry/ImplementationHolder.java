package io.github.toberocat.improvedFactions.registry;

import io.github.toberocat.improvedFactions.faction.components.rank.Rank;
import io.github.toberocat.improvedFactions.handler.ColorHandler;
import io.github.toberocat.improvedFactions.handler.ConfigHandler;
import io.github.toberocat.improvedFactions.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.handler.ItemHandler;
import org.jetbrains.annotations.Nullable;

/**
 * These class contains all interfaces that require to get populated when creating a new implementation
 */
public class ImplementationHolder {
    public static @Nullable ConfigHandler configHandler;
    public static @Nullable ColorHandler colorHandler;
    public static @Nullable RankHolder rankHolder;
    public static @Nullable ItemHandler itemHandler;
    public static @Nullable ClaimHandler claimHandler;

    public static @Nullable ImprovedFactions improvedFactions;

    /**
     * This should get called when all core registers should get called
     */
    public static void register() {
        Rank.register();
    }
}
