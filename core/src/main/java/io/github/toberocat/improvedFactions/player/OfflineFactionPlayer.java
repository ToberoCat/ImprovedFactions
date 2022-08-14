package io.github.toberocat.improvedFactions.player;

import io.github.toberocat.improvedFactions.faction.Faction;
import io.github.toberocat.improvedFactions.persistent.PersistentDataContainer;
import io.github.toberocat.improvedFactions.translator.Placeholder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface OfflineFactionPlayer<P> {
    /* Faction */
    @NotNull Faction<?> getFaction();
    @Nullable String getFactionRegistry();

    boolean inFaction();

    /* Messages */

    void sendMessage(@NotNull String message);

    void sendTranslatable(@NotNull String key, Placeholder... placeholders);

    /* Player */
    @Nullable FactionPlayer<?> getPlayer();
    @NotNull UUID getUniqueId();
    @NotNull String getName();
    long getLastPlayed();
    boolean isOnline();

    /* Persistent data */
    @NotNull PersistentDataContainer getDataContainer();

    /* Raw instance */
    @NotNull P getRaw();
}
