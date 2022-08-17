package io.github.toberocat.improvedFactions.core.player;

import io.github.toberocat.improvedFactions.core.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.improvedFactions.core.utils.ReturnConsumer;
import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.persistent.component.PersistentWrapper;
import io.github.toberocat.improvedFactions.core.translator.Placeholder;
import io.github.toberocat.improvedFactions.core.translator.layout.Translatable;
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
    @NotNull PersistentWrapper getDataContainer();

    /* Raw instance */
    @NotNull P getRaw();
}
