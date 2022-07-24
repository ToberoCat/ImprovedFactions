package io.github.toberocat.core.factions;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.factions.components.Description;
import io.github.toberocat.core.factions.components.FactionClaims;
import io.github.toberocat.core.factions.components.FactionModule;
import io.github.toberocat.core.factions.components.rank.Rank;
import io.github.toberocat.core.factions.components.rank.members.FactionRank;
import io.github.toberocat.core.utility.ForbiddenChecker;
import io.github.toberocat.core.utility.exceptions.faction.FactionIsFrozenException;
import io.github.toberocat.core.utility.exceptions.setting.SettingNotFoundException;
import io.github.toberocat.core.utility.settings.type.RankSetting;
import io.github.toberocat.core.utility.settings.type.Setting;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.stream.Stream;

public abstract class Faction<F extends Faction<F>> {
    /* Static vars */
    public static final int allyId = 0;
    public static final int neutralId = 1;
    public static final int enemyId = 2;

    /* Config values */
    public static final long activeThreshold = (long) MainIF.config().getInt("faction.active-member-threshold", 60);

    /* Static voids */

    /**
     * It takes a string, removes all non-alphabetic characters, and returns the result
     *
     * @param display The display name of the faction.
     * @return A string that is the display name of the faction, but with the following changes:
     * - The string is truncated to the maximum length of a faction name.
     * - The string is converted to lowercase.
     * - All color codes are stripped from the string.
     * - All non-alphabetic characters are removed from the string.
     */
    public static @NotNull String displayToRegistry(@NotNull String display) {
        return display
                .substring(0, MainIF.config().getInt("faction.maxNameLen"))
                .toLowerCase()
                .transform(ChatColor::stripColor)
                .replaceAll("[^a-z]", "");
    }

    /**
     * If the name is 'safezone' or 'warzone', return false. Otherwise, return the result of the
     * ForbiddenChecker.checkName() function.
     * <p>
     * The ForbiddenChecker.checkName() function is defined in the same file, and it's a bit more complicated.
     * It checks if a word has an high similarity to an forbidden one
     *
     * @param name The name of the faction
     * @return If the name is valid
     */
    public static boolean validNaming(@NotNull String name) {
        if (name.equalsIgnoreCase("safezone") ||
                name.equalsIgnoreCase("warzone")) return false;
        return ForbiddenChecker.checkName(name);
    }

    /* Faction infos */

    /**
     * It creates populates the faction by loading everything it needs from storage
     *
     * @param loadRegistry The registry of the faction to load.
     */
    public abstract void createFromStorage(@NotNull String loadRegistry);

    /* Getters */

    /**
     * Returns the registry of the faction.
     *
     * @return The registry.
     */
    @NotNull
    public abstract String getRegistry();

    /**
     * Returns the display name of the faction.
     *
     * @return The display name. Can contain:
     * - Color (§a, etc.)
     * - Special characters
     */
    @NotNull
    public abstract String getDisplay();

    /**
     * Get the faction color
     * @return The color of the faction.
     */
    public abstract int getColor() throws SettingNotFoundException;

    /* Setter */

    /**
     * Sets the display name of the faction
     *
     * @param display The display name of the faction.
     */
    public abstract void setDisplay(@NotNull String display) throws FactionIsFrozenException;

    /**
     * Returns the message of the day.
     *
     * @return The message of the day.
     */
    @NotNull
    public abstract String getMotd();

    /**
     * Sets the faction's MOTD
     *
     * @param motd The message of the day.
     */
    public abstract void setMotd(@NotNull String motd) throws FactionIsFrozenException;

    /**
     * Returns the tag of this faction
     *
     * @return A string, whose max length is limited to the tag length
     */
    @NotNull
    public abstract String getTag();

    /**
     * Sets the tag of the faction
     *
     * @param tag The tag to set.
     */
    public abstract void setTag(@NotNull String tag) throws FactionIsFrozenException;

    /**
     * Returns an instance of the anonymous description class
     *
     * @return The description object
     */
    @NotNull
    public abstract Description getDescription();

    /**
     * Returns the date and time when the faction was created.
     *
     * @return A string
     */
    @NotNull
    public abstract String getCreatedAt();

    /**
     * Returns the type of the faction.
     *
     * @return The type
     */
    @NotNull
    public abstract OpenType getType();

    /**
     * Sets the type of the faction
     *
     * @param type The type
     */
    public abstract void setType(@NotNull OpenType type) throws FactionIsFrozenException;

    /**
     * Returns the owner of this faction.
     *
     * @return The owner of the faction.
     */
    @NotNull
    public abstract UUID getOwner();

    /**
     * Returns true if the faction is permanent.
     * A permanent faction can exist without members in it
     *
     * @return If permanent
     */
    public abstract boolean isPermanent();

    /**
     * Sets whether the faction is permanent or not
     *
     * @param permanent If true, the faction will be stored permanently. If false, the faction needs to be
     *                  deleted when the owner tries to leave
     */
    public abstract void setPermanent(boolean permanent);

    /**
     * Returns true if the faction is frozen, otherwise returns false.
     *
     * @return If frozen
     */
    public abstract boolean isFrozen();

    /**
     * Sets the frozen state of the faction
     * Frozen factions can't change their current state
     *
     * @param frozen true if the faction is frozen, false if not
     */
    public abstract void setFrozen(boolean frozen);

    /* Rank management */

    /**
     * Gets the rank of the specified player.
     *
     * @param player The player to get the rank of.
     * @return The rank of the player.
     */
    @NotNull
    public abstract Rank getPlayerRank(@NotNull OfflinePlayer player);

    /**
     * Gets the permission setting for the specified permission
     *
     * @param permission The permission you want to get the setting for.
     * @return A RankSetting instance.
     */
    @NotNull
    public abstract RankSetting getPermission(@NotNull String permission) throws SettingNotFoundException;

    /**
     * Returns whether the player has the given permission
     *
     * @param player     The player to check the permission for.
     * @param permission The permission to check for.
     * @return If the permission is allowed for the specified player
     */
    public abstract boolean hasPermission(@NotNull OfflinePlayer player, @NotNull String permission)
            throws SettingNotFoundException;

    /**
     * Returns true if the player is a member of the faction
     *
     * @param player The player to check.
     * @return If the player is in the faction
     */
    public abstract boolean isMember(@NotNull OfflinePlayer player);

    /**
     * Changes the rank of the specified player to the specified rank
     *
     * @param player The player you want to change the rank of.
     * @param rank   The rank you want to change the player to.
     */
    public abstract void changeRank(@NotNull OfflinePlayer player, @NotNull FactionRank rank) throws FactionIsFrozenException;

    /* Faction management */

    /**
     * Transfer ownership of the faction to the specified player.
     *
     * @param player The player who will be the new owner of the faction.
     */
    public abstract void transferOwnership(@NotNull Player player) throws FactionIsFrozenException;

    /**
     * Deletes the faction
     */
    public abstract void deleteFaction() throws FactionIsFrozenException;

    /* Member management */

    /**
     * Returns a stream of all banned players.
     *
     * @return A stream of UUIDs
     */
    @NotNull
    public abstract Stream<UUID> getBanned();

    /**
     * Returns a stream of all the members of this faction.
     *
     * @return A stream of UUIDs
     */
    @NotNull
    public abstract Stream<UUID> getMembers();

    @NotNull
    public Stream<OfflinePlayer> getPlayers() {
        return getMembers().map(Bukkit::getOfflinePlayer);
    }

    public Stream<OfflinePlayer> getActiveMembers() {
        return getPlayers().filter(x -> Math.floor((System.currentTimeMillis() -
                x.getLastPlayed()) / 0.000000011574) <= activeThreshold);
    }

    public Stream<OfflinePlayer> getOnlineMembers() {
        return getPlayers().filter(OfflinePlayer::isOnline);
    }


    /**
     * joinPlayer joins a player to the faction
     *
     * @param player The player to join the game.
     * @return If it was able to join
     */
    public abstract boolean joinPlayer(@NotNull Player player) throws FactionIsFrozenException;

    /**
     * Join a player in a faction to a rank.
     *
     * @param player The player to join the faction.
     * @param rank   The rank that the player will be joining as.
     * @return If the player was able to join
     */
    public abstract boolean joinPlayer(@NotNull Player player, @NotNull Rank rank) throws FactionIsFrozenException;

    /**
     * Removes a player from the faction
     *
     * @param player The player to leave the faction.
     * @return If the player was able to leave
     */
    public abstract boolean leavePlayer(@NotNull Player player) throws FactionIsFrozenException;

    /**
     * This function kicks a player from the faction
     *
     * @param player The player to kick.
     * @return If the player was able to kicked
     */
    public abstract boolean kickPlayer(@NotNull OfflinePlayer player) throws FactionIsFrozenException;

    /**
     * This function bans a player.
     *
     * @param player The player to ban.
     * @return If the player was able to get banned
     */
    public abstract boolean banPlayer(@NotNull OfflinePlayer player) throws FactionIsFrozenException;

    /**
     * Pardon a player from the ban list.
     *
     * @param player The player to pardon.
     * @return If the player was able to be pardoned
     */
    public abstract boolean pardonPlayer(@NotNull OfflinePlayer player) throws FactionIsFrozenException;

    /**
     * Returns true if the player is banned, false otherwise.
     *
     * @param player The player to check
     * @return If banned or not
     */
    public abstract boolean isBanned(@NotNull OfflinePlayer player);


    /* Power management */

    /**
     * Returns the power of the faction
     * It sums all player power
     *
     * @return A BigDecimal representing the total power
     */
    @NotNull
    public abstract BigDecimal getTotalPower();

    /**
     * Returns the power summed of
     * all players that have been online
     * the last few days
     *
     * @return A BigDecimal representing the active power
     */
    @NotNull
    public abstract BigDecimal getActivePower();

    /**
     * Returns the maximum power that can be generated by the faction
     * It sums all player's maxpower
     *
     * @return A BigDecimal representing the max reachable power
     */
    @NotNull
    public abstract BigDecimal getTotalMaxPower();

    /**
     * Returns the max power summed of
     * all players that have been online
     * the last few days
     *
     * @return A BigDecimal representing the active max power
     */
    @NotNull
    public abstract BigDecimal getActiveMaxPower();

    /**
     * Returns the power of the player with the given UUID.
     * *
     *
     * @param player The player's UUID
     * @return The player power
     */
    public abstract double playerPower(@NotNull UUID player);

    /**
     * Returns the maximum power of the given player.
     *
     * @param player The player's UUID
     * @return The maximum power of the player.
     */
    public abstract double maxPlayerPower(@NotNull UUID player);

    /* Relations */

    /**
     * Adds an ally to the faction
     *
     * @param faction The faction to add as an ally.
     * @return If the ally was able got added
     */
    public abstract boolean addAlly(@NotNull F faction) throws FactionIsFrozenException;

    /**
     * Returns true if the given registry is allied with this faction.
     *
     * @param registry The registry of the faction you want to check.
     * @return If allied or not
     */
    public abstract boolean isAllied(@NotNull String registry);

    /**
     * Returns true if the player is in an allied faction of this.
     *
     * @param player The player to check.
     * @return If allied or not
     */
    public abstract boolean isAllied(@NotNull OfflinePlayer player);

    /**
     * Adds an enemy to the faction.
     *
     * @param faction The faction to add as an enemy.
     * @return If the faction got added as enemy
     */
    public abstract boolean addEnemy(@NotNull F faction) throws FactionIsFrozenException;

    /**
     * This function returns true if the given registry is an enemy faction.
     *
     * @param registry The registry name of the enemy.
     * @return If enemies or not
     */
    public abstract boolean isEnemy(@NotNull String registry);

    /**
     * Returns true if the given player is an enemy of this faction

     * @param player The player to check.
     * @return If the player's faction is an enemy
     */
    public abstract boolean isEnemy(@NotNull OfflinePlayer player);

    /**
     * Returns a stream of all the allies of this faction.
     *
     * @return A stream of allied faction registries.
     */
    @NotNull
    public abstract Stream<String> getAllies();

    /**
     * Returns a stream of enemy faction registries.
     *
     * @return A stream of enemy faction registries
     */
    @NotNull
    public abstract Stream<String> getEnemies();

    /**
     * Resets the relation of the given faction to the default relation.
     *
     * @param faction The faction to reset the relation of.
     * @return If successfully reseted.
     */
    public abstract boolean resetRelation(@NotNull F faction) throws FactionIsFrozenException;

    /* Claim management */

    /**
     * Returns the claims of this faction.
     *
     * @return A list of claims.
     */
    @NotNull
    public abstract FactionClaims<F> getClaims();

    /* Settings */

    /**
     * Get a setting by name
     *
     * @param setting The name of the setting you want to get.
     * @return A setting instance
     */
    @NotNull
    public abstract Setting<?> getSetting(@NotNull String setting) throws SettingNotFoundException;

    /* Module management */

    /**
     * Returns the module with the given name, or null if no such module exists
     *
     * @param moduleRegistry The name of the module you want to get.
     * @return A module that is registered to the module registry.
     */
    @Nullable
    public abstract FactionModule<F> getModule(@NotNull String moduleRegistry);

    /**
     * It creates a new instance of the class you pass in, and adds it to the list of modules
     *
     * @param clazz      The class of the module you want to create.
     * @param parameters The values you want to feed into the constructor
     * @param <C>        The module type
     */
    public abstract <C extends FactionModule<F>> void createModule(@NotNull Class<C> clazz, Object... parameters)
            throws NoSuchMethodException, InvocationTargetException,
            InstantiationException, IllegalAccessException;
}
