package io.github.toberocat.improvedFactions.core.permission;

import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.core.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.improvedFactions.core.faction.components.rank.Rank;
import io.github.toberocat.improvedFactions.core.player.OfflineFactionPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

/**
 * This interface is for all classes that require a permission handler
 */
public interface Permissions {

    /**
     * Get a stream of all ranks that are allowed to use this permission
     *
     * @param permission The permission to get the ranks from
     * @return A stream of allowed ranks.
     */
    @NotNull Stream<String> getPermission(@NotNull Permission permission);

    /**
     * Returns a stream of all permissions.
     *
     * @return A stream of permissions.
     */
    @NotNull Stream<Permission> listPermissions();

    /**
     * Returns a stream of all permissions that are assigned to the given rank.
     *
     * @param rank The rank to list the permissions of.
     * @return A stream of permissions.
     */
    @NotNull Stream<Permission> listPermissions(@NotNull Rank rank);

    /**
     * Sets the allowed ranks for the specified permission
     *
     * @param permission The ranks to set.
     * @param ranks The permission that the ranks will be allowed to.
     */
    void setPermission(@NotNull Permission permission, String[] ranks);

    /**
     * Returns true if the given permission has the given rank.
     *
     * @param permission The permission you want to check.
     * @param rank The rank to check the permission for.
     * @return If the rank has the specified permission
     */
    boolean hasPermission(@NotNull Permission permission, @NotNull Rank rank);

    /**
     * Returns true if the given sender has the given permission.
     *
     * @param permission The permission you want to check.
     * @param player The sender to check the permission for.
     * @return If the sender has the specified permission
     */
    default boolean hasPermission(@NotNull Permission permission, @NotNull OfflineFactionPlayer player)
            throws FactionNotInStorage, PlayerHasNoFactionException {
        return hasPermission(permission, player.getRank());
    }

}
