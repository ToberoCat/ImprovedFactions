package io.github.toberocat.core.factions;

import io.github.toberocat.core.factions.claim.FactionClaims;
import io.github.toberocat.core.factions.local.rank.Rank;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.UUID;

public interface Faction {
    /* Faction identifier */
    @NotNull String getRegistry();
    @NotNull String getDisplay();
    @NotNull OpenType getType();

    /* Rank management */
    @NotNull Rank getPlayerRank(@Nullable OfflinePlayer player);
    boolean hasPermission(@NotNull OfflinePlayer player, @NotNull String permission);
    boolean isMember(@NotNull UUID player);
    void changeRank(@NotNull OfflinePlayer player, @NotNull Rank rank);

    /* Faction management */
    void transferOwnership(@NotNull Player player);
    void deleteFaction();

    /* Member management */
    boolean joinPlayer(@NotNull Player player);
    boolean joinPlayer(@NotNull Player player, @NotNull Rank rank);
    boolean joinPlayer(@NotNull Player player, @NotNull UUID inviteId);

    boolean leavePlayer(@NotNull Player player);

    boolean kickPlayer(@NotNull OfflinePlayer player);
    boolean banPlayer(@NotNull OfflinePlayer player);
    boolean pardonPlayer(@NotNull OfflinePlayer player);

    /* Power management */
    @NotNull BigDecimal getPower();
    double playerPower(@NotNull OfflinePlayer player);

    /* Relations */
    boolean addAlly(@NotNull Faction faction);
    boolean addEnemy(@NotNull Faction faction);
    boolean resetRelation(@NotNull Faction faction);

    /* Claim management */
    FactionClaims getClaims();

    /* Module management */
    @Nullable <C> C getModule(@NotNull Class<C> clazz);
    <C> void createModule(@NotNull Class<C> clazz, Object... parameters);
}
