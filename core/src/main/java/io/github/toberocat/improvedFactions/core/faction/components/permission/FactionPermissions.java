package io.github.toberocat.improvedFactions.core.faction.components.permission;

import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.core.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.improvedFactions.core.faction.components.rank.Rank;
import io.github.toberocat.improvedFactions.core.sender.player.OfflineFactionPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public interface FactionPermissions {
    @NotNull Stream<String> getPermission(@NotNull String permission);

    @NotNull Stream<String> listPermissions();

    @NotNull Stream<String> listPermissions(@NotNull Rank rank);

    void setPermission(@NotNull String permission, String[] ranks);

    boolean hasPermission(@NotNull String permission, @NotNull Rank rank);

    default boolean hasPermission(@NotNull String permission, @NotNull OfflineFactionPlayer<?> player)
            throws FactionNotInStorage, PlayerHasNoFactionException {
        return hasPermission(permission, player.getRank());
    }

}
