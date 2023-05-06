package io.github.toberocat.improvedFactions.core.player;

import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.core.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.faction.components.rank.Rank;
import io.github.toberocat.improvedFactions.core.persistent.component.PersistentWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

public interface OfflineFactionPlayer {
    /* Faction */
    default @NotNull Rank getRank() throws FactionNotInStorage, PlayerHasNoFactionException {
        return getFaction().getPlayerRank(this);
    }

    @NotNull Faction<?> getFaction() throws PlayerHasNoFactionException, FactionNotInStorage;

    @Nullable String getFactionRegistry();


    boolean inFaction();

    /* Messages */
    void sendMessage(@NotNull String query, @NotNull Map<String, String> placeholders);

    /* Player */
    @Nullable FactionPlayer getPlayer();

    @NotNull UUID getUniqueId();

    @NotNull String getName();

    long getLastPlayed();

    boolean isOnline();

    /* Persistent data */
    @NotNull PersistentWrapper getDataContainer();

    /* Raw instance */
    @NotNull Object getRaw();
}
