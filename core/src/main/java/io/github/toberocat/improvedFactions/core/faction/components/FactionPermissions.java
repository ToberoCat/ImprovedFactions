package io.github.toberocat.improvedFactions.core.faction.components;

import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.core.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.improvedFactions.core.faction.components.rank.Rank;
import io.github.toberocat.improvedFactions.core.sender.player.OfflineFactionPlayer;
import org.jetbrains.annotations.NotNull;

public interface FactionPermissions {
    @NotNull String[] getPermission(@NotNull String permission);

    void setPermission(@NotNull String permission, String[] ranks);

    boolean hasPermission(@NotNull String permission, @NotNull Rank rank);

    default boolean hasPermission(@NotNull String permission, @NotNull OfflineFactionPlayer<?> player)
            throws FactionNotInStorage, PlayerHasNoFactionException {
        return hasPermission(permission, player.getRank());
    }

}
