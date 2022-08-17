package io.github.toberocat.improvedFactions.registry;

import io.github.toberocat.improvedFactions.claims.ClaimHandler;
import io.github.toberocat.improvedFactions.event.EventListener;
import io.github.toberocat.improvedFactions.faction.components.rank.Rank;
import io.github.toberocat.improvedFactions.handler.*;
import io.github.toberocat.improvedFactions.persistent.PersistentHandler;
import io.github.toberocat.improvedFactions.persistent.local.LocalData;
import io.github.toberocat.improvedFactions.player.FactionPlayer;
import io.github.toberocat.improvedFactions.translator.Translation;
import io.github.toberocat.improvedFactions.translator.layout.Translatable;
import io.github.toberocat.improvedFactions.utils.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

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
    public static @Nullable MessagingHandler messagingHandler;
    public static @Nullable Logger logger;

    public static @Nullable ImprovedFactions improvedFactions;

    /**
     * This should get called when all core registers should get called
     */
    public static void register() throws IOException {
        Rank.register();
        ClaimHandler.api().cacheAllWorlds();
        Translation.createLocaleMap();
    }

    /**
     * Call it when you don't need any of the core features / want to reload them using a sequence,
     * like:
     *
     * // Reload all handlers
     * dispose();
     * register();
     */
    public static void dispose() {
        ClaimHandler.api().dispose();
        Translation.dispose();
        PersistentHandler.api().dispose();
    }

    public static void playerJoin(@NotNull FactionPlayer<?> player) {
        Translation.playerJoin(player);
    }

    public static void playerLeave(@NotNull FactionPlayer<?> player) {
        Translation.playerLeave(player);
        PersistentHandler.api().quit(player);
    }
}
