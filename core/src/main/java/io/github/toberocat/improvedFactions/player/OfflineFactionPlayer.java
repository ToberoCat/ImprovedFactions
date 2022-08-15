package io.github.toberocat.improvedFactions.player;

import io.github.toberocat.improvedFactions.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.improvedFactions.faction.Faction;
import io.github.toberocat.improvedFactions.translator.Placeholder;
import io.github.toberocat.improvedFactions.translator.layout.Translatable;
import io.github.toberocat.improvedFactions.utils.ReturnConsumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface OfflineFactionPlayer<P> {
    /* Faction */
    @NotNull Faction<?> getFaction() throws PlayerHasNoFactionException, FactionNotInStorage;
    @Nullable String getFactionRegistry();

    boolean inFaction();

    /* Messages */

    void sendMessage(@NotNull String message);

    void sendTranslatable(@NotNull ReturnConsumer<Translatable, String> query,
                          Placeholder... placeholders);

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
