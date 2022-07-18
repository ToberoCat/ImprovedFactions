package io.github.toberocat.core.factions;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.factions.components.Description;
import io.github.toberocat.core.factions.components.FactionClaims;
import io.github.toberocat.core.factions.components.FactionModule;
import io.github.toberocat.core.factions.components.rank.Rank;
import io.github.toberocat.core.factions.components.rank.members.FactionRank;
import io.github.toberocat.core.utility.ForbiddenChecker;
import io.github.toberocat.core.utility.exceptions.setting.SettingNotFoundException;
import io.github.toberocat.core.utility.settings.type.RankSetting;
import io.github.toberocat.core.utility.settings.type.Setting;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.stream.Stream;

public interface Faction<F extends Faction<F>> {
    /* Static vars */
    int allyId = 0;
    int neutralId = 1;
    int enemyId = 2;

    /* Static voids */
    static @NotNull String displayToRegistry(@NotNull String display) {
        return display
                .substring(0, MainIF.config().getInt("faction.maxNameLen"))
                .toLowerCase()
                .transform(ChatColor::stripColor)
                .replaceAll("[^a-z]", "");
    }

    static boolean validNaming(@NotNull String name) {
        if (name.equalsIgnoreCase("safezone") ||
                name.equalsIgnoreCase("warzone")) return false;
        return ForbiddenChecker.checkName(name);
    }

    /* Faction infos */
    void createFromStorage(@NotNull String loadRegistry);

    /* Getters */
    @NotNull String getRegistry();

    @NotNull String getDisplay();

    /* Setter */
    void setDisplay(@NotNull String display);

    @NotNull String getMotd();

    void setMotd(@NotNull String motd);

    @NotNull String getTag();

    void setTag(@NotNull String tag);

    @NotNull Description getDescription();

    @NotNull String getCreatedAt();

    @NotNull OpenType getType();

    void setType(@NotNull OpenType type);

    @NotNull UUID getOwner();

    boolean isPermanent();

    void setPermanent(boolean permanent);

    boolean isFrozen();

    void setFrozen(boolean frozen);

    /* Rank management */
    @NotNull Rank getPlayerRank(@NotNull OfflinePlayer player);

    @NotNull RankSetting getPermission(@NotNull String permission) throws SettingNotFoundException;

    boolean hasPermission(@NotNull OfflinePlayer player, @NotNull String permission)
            throws SettingNotFoundException;

    boolean isMember(@NotNull OfflinePlayer player);

    void changeRank(@NotNull OfflinePlayer player, @NotNull FactionRank rank);

    /* Faction management */
    void transferOwnership(@NotNull Player player);

    void deleteFaction();

    /* Member management */
    @NotNull Stream<UUID> getBanned();

    @NotNull Stream<UUID> getMembers();

    boolean joinPlayer(@NotNull Player player);

    boolean joinPlayer(@NotNull Player player, @NotNull Rank rank);

    boolean leavePlayer(@NotNull Player player);

    boolean kickPlayer(@NotNull OfflinePlayer player);

    boolean banPlayer(@NotNull OfflinePlayer player);

    boolean pardonPlayer(@NotNull OfflinePlayer player);

    boolean isBanned(@NotNull OfflinePlayer player);


    /* Power management */
    @NotNull BigDecimal getPower();

    @NotNull BigDecimal getMaxPower();

    double playerPower(@NotNull UUID player);

    double maxPlayerPower(@NotNull UUID player);

    /* Relations */
    boolean addAlly(@NotNull F faction);

    boolean isAllied(@NotNull String registry);

    boolean isAllied(@NotNull OfflinePlayer player);

    boolean addEnemy(@NotNull F faction);

    boolean isEnemy(@NotNull String registry);

    @NotNull Stream<String> getAllies();

    @NotNull Stream<String> getEnemies();

    boolean resetRelation(@NotNull F faction);

    /* Claim management */
    @NotNull FactionClaims getClaims();

    /* Settings */
    @NotNull Setting<?> getSetting(@NotNull String setting) throws SettingNotFoundException;

    /* Module management */
    @Nullable FactionModule<F> getModule(@NotNull String moduleRegistry);

    <C extends FactionModule<F>> void createModule(@NotNull Class<C> clazz, Object... parameters)
            throws NoSuchMethodException, InvocationTargetException,
            InstantiationException, IllegalAccessException;
}
