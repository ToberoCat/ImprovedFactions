package io.github.toberocat.improvedFactions.registry;

import io.github.toberocat.improvedFactions.claims.ClaimHandler;
import io.github.toberocat.improvedFactions.event.EventListener;
import io.github.toberocat.improvedFactions.faction.components.rank.Rank;
import io.github.toberocat.improvedFactions.handler.*;
import io.github.toberocat.improvedFactions.utils.Logger;
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
    public static @Nullable DatabaseHandler databaseHandler;
    public static @Nullable EventListener eventHandler;
    public static @Nullable Logger logger;

    public static @Nullable ImprovedFactions improvedFactions;

    /**
     * This should get called when all core registers should get called
     */
    public static void register() {
        Rank.register();
        ClaimHandler.api().cacheAllWorlds();
    }

    /**
     * Call it when you don't need any of the core features any more / want to reload them using a sequence,
     * like:
     *
     * // Reload all handlers
     * dispose();
     * register();
     */
    public static void dispose() {
        ClaimHandler.api().dispose();
    }
}
