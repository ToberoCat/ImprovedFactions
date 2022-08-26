package io.github.toberocat.improvedFactions.core.permission;

import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.core.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.improvedFactions.core.faction.components.rank.Rank;
import io.github.toberocat.improvedFactions.core.sender.player.OfflineFactionPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public interface FactionPermissions {

    @NotNull Stream<String> getPermission(@NotNull Permission permission);

    @NotNull Stream<Permission> listPermissions();

    @NotNull Stream<Permission> listPermissions(@NotNull Rank rank);

    void setPermission(@NotNull Permission permission, String[] ranks);

    boolean hasPermission(@NotNull Permission permission, @NotNull Rank rank);

    default boolean hasPermission(@NotNull Permission permission, @NotNull OfflineFactionPlayer<?> player)
            throws FactionNotInStorage, PlayerHasNoFactionException {
        return hasPermission(permission, player.getRank());
    }

}
