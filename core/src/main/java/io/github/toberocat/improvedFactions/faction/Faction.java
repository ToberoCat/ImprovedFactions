package io.github.toberocat.improvedFactions.faction;

import io.github.toberocat.improvedFactions.exceptions.faction.FactionIsFrozenException;
import io.github.toberocat.improvedFactions.exceptions.faction.FactionOwnerIsOfflineException;
import io.github.toberocat.improvedFactions.exceptions.faction.leave.PlayerIsOwnerException;
import io.github.toberocat.improvedFactions.exceptions.faction.relation.AlreadyInvitedException;
import io.github.toberocat.improvedFactions.exceptions.faction.relation.CantInviteYourselfException;
import io.github.toberocat.improvedFactions.faction.components.Description;
import io.github.toberocat.improvedFactions.faction.components.FactionClaims;
import io.github.toberocat.improvedFactions.faction.components.FactionModule;
import io.github.toberocat.improvedFactions.faction.components.rank.Rank;
import io.github.toberocat.improvedFactions.faction.components.rank.members.FactionRank;
import io.github.toberocat.improvedFactions.faction.components.report.FactionReports;
import io.github.toberocat.improvedFactions.handler.ColorHandler;
import io.github.toberocat.improvedFactions.handler.ConfigHandler;
import io.github.toberocat.improvedFactions.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.player.FactionPlayer;
import io.github.toberocat.improvedFactions.player.OfflineFactionPlayer;
import io.github.toberocat.improvedFactions.setting.component.SettingHolder;
import io.github.toberocat.improvedFactions.translator.Placeholder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

public interface Faction<F extends Faction<F>> extends SettingHolder {
    /* Static vars */
    int allyId = 0;
    int neutralId = 1;
    int enemyId = 2;

    /* Config values */
    long activeThreshold = ConfigHandler.api().getInt("faction.active-member-threshold", 60);

    /* Default values */

    String DEFAULT_MOTD = ConfigHandler.api().getString("faction.default.motd", "Newly created faction");
    String DEFAULT_TAG = ConfigHandler.api().getString("faction.default.tag", "IFF");

    boolean DEFAULT_FROZEN = ConfigHandler.api().getBool("faction.default.frozen");
    boolean DEFAULT_PERMANENT = ConfigHandler.api().getBool("faction.default.permanent");

    long DEFAULT_START_BALANCE = ConfigHandler.api().getLong("faction.default.start-balance");

    OpenType DEFAULT_OPEN_TYPE = OpenType.valueOf(ConfigHandler.api()
            .getString("faction.default.open-type", "INVITE_ONLY"));

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
    static @NotNull String displayToRegistry(@NotNull String display) {
        return display
                .substring(0, ConfigHandler.api().getInt("faction.max-registry-length", 10))
                .toLowerCase()
                .transform(x -> ColorHandler.api().stripColor(x))
                .replaceAll("[^a-z]", "");
    }

    static @NotNull String validateDisplay(@NotNull String display) {
        return display
                .substring(0, ConfigHandler.api().getInt("faction.max-display-length", 10));
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
    static boolean validNaming(@NotNull String name) {
        return !name.equalsIgnoreCase("safezone") &&
                !name.equalsIgnoreCase("warzone");
    }

    /* Faction infos */

    /* Getters */

    /**
     * Returns the registry of the faction.
     *
     * @return The registry.
     */
    @NotNull String getRegistry();

    /**
     * Returns the display name of the faction.
     *
     * @return The display name. Can contain:
     * - Color (§a, etc.)
     * - Special characters
     */
    @NotNull String getDisplay();

    /**
     * Sets the display name of the faction
     *
     * @param display The display name of the faction.
     */
    void setDisplay(@NotNull String display) throws FactionIsFrozenException;

    /* Setter */

    /**
     * Get the faction color
     *
     * @return The color of the faction.
     */
    int getColor();

    /**
     * Returns the message of the day.
     *
     * @return The message of the day.
     */
    @NotNull String getMotd();

    /**
     * Sets the faction's MOTD
     *
     * @param motd The message of the day.
     */
    void setMotd(@NotNull String motd) throws FactionIsFrozenException;

    /**
     * Returns the tag of this faction
     *
     * @return A string, whose max length is limited to the tag length
     */
    @NotNull String getTag();

    /**
     * Sets the tag of the faction
     *
     * @param tag The tag to set.
     */
    void setTag(@NotNull String tag) throws FactionIsFrozenException;

    /**
     * Returns an instance of the anonymous description class
     *
     * @return The description object
     */
    @NotNull Description getDescription();

    /**
     * Returns the date and time when the faction was created.
     *
     * @return A string
     */
    @NotNull String getCreatedAt();

    /**
     * Returns the type of the faction.
     *
     * @return The type
     */
    @NotNull OpenType getType();

    /**
     * Sets the type of the faction
     *
     * @param type The type
     */
    void setType(@NotNull OpenType type) throws FactionIsFrozenException;

    /**
     * Returns the owner of this faction.
     *
     * @return The owner of the faction.
     */
    @NotNull UUID getOwner();

    /**
     * Returns true if the faction is permanent.
     * A permanent faction can exist without members in it
     *
     * @return If permanent
     */
    boolean isPermanent();

    /**
     * Sets whether the faction is permanent or not
     *
     * @param permanent If true, the faction will be stored permanently. If false, the faction needs to be
     *                  deleted when the owner tries to leave
     */
    void setPermanent(boolean permanent);

    /**
     * Returns true if the faction is frozen, otherwise returns false.
     *
     * @return If frozen
     */
    boolean isFrozen();

    /**
     * Sets the frozen state of the faction
     * Frozen factions can't change their current state
     *
     * @param frozen true if the faction is frozen, false if not
     */
    void setFrozen(boolean frozen);

    /* Rank management */

    /**
     * Gets the rank of the specified player.
     *
     * @param player The player to get the rank of.
     * @return The rank of the player.
     */
    @NotNull Rank getPlayerRank(@NotNull OfflineFactionPlayer<?> player);

    /**
     * Returns true if the player is a member of the faction
     *
     * @param player The player to check.
     * @return If the player is in the faction
     */
    boolean isMember(@NotNull OfflineFactionPlayer<?> player);

    /**
     * Changes the rank of the specified player to the specified rank
     *
     * @param player The player you want to change the rank of.
     * @param rank   The rank you want to change the player to.
     */
    void changeRank(@NotNull OfflineFactionPlayer<?> player, @NotNull FactionRank rank)
            throws FactionIsFrozenException;

    /* Faction management */

    /**
     * Transfer ownership of the faction to the specified player.
     *
     * @param player The player who will be the new owner of the faction.
     */
    void transferOwnership(@NotNull FactionPlayer<?> player) throws FactionIsFrozenException;

    /**
     * Deletes the faction
     */
    void deleteFaction() throws FactionIsFrozenException;

    /* Member management */

    /**
     * Returns a stream of all banned players.
     *
     * @return A stream of UUIDs
     */
    @NotNull Stream<UUID> getBanned();

    /**
     * Returns a stream of all the members of this faction.
     *
     * @return A stream of UUIDs
     */
    @NotNull Stream<UUID> getMembers();

    @NotNull
    default Stream<OfflineFactionPlayer<?>> getPlayers() {
        return getMembers()
                .map(x -> ImprovedFactions.api().getOfflinePlayer(x));
    }

    default Stream<OfflineFactionPlayer<?>> getActiveMembers() {
        return getPlayers().filter(x -> Math.floor((System.currentTimeMillis() -
                x.getLastPlayed()) / 0.000000011574) <= activeThreshold);
    }

    default Stream<OfflineFactionPlayer<?>> getOnlineMembers() {
        return getPlayers().filter(OfflineFactionPlayer::isOnline);
    }


    /**
     * joinPlayer joins a player to the faction
     *
     * @param player The player to join the game.
     * @return If it was able to join
     */
    boolean joinPlayer(@NotNull FactionPlayer<?> player) throws FactionIsFrozenException;

    /**
     * Join a player in a faction to a rank.
     *
     * @param player The player to join the faction.
     * @param rank   The rank that the player will be joining as.
     * @return If the player was able to join
     */
    boolean joinPlayer(@NotNull FactionPlayer<?> player, @NotNull FactionRank rank) throws FactionIsFrozenException;

    /**
     * Removes a player from the faction
     *
     * @param player The player to leave the faction.
     * @return If the player was able to leave
     */
    boolean leavePlayer(@NotNull FactionPlayer<?> player) throws FactionIsFrozenException, PlayerIsOwnerException;

    /**
     * This function kicks a player from the faction
     *
     * @param player The player to kick.
     * @return If the player was able to kicked
     */
    boolean kickPlayer(@NotNull OfflineFactionPlayer<?> player) throws FactionIsFrozenException;

    /**
     * This function bans a player.
     *
     * @param player The player to ban.
     * @return If the player was able to get banned
     */
    boolean banPlayer(@NotNull OfflineFactionPlayer<?> player) throws FactionIsFrozenException;

    /**
     * Pardon a player from the ban list.
     *
     * @param player The player to pardon.
     * @return If the player was able to be pardoned
     */
    boolean pardonPlayer(@NotNull OfflineFactionPlayer<?> player) throws FactionIsFrozenException;

    /**
     * Returns true if the player is banned, false otherwise.
     *
     * @param player The player to check
     * @return If banned or not
     */
    boolean isBanned(@NotNull OfflineFactionPlayer<?> player);


    /* Power management */

    /**
     * Returns the power of the faction
     * It sums all player power
     *
     * @return A BigDecimal representing the total power
     */
    @NotNull BigDecimal getTotalPower();

    /**
     * Returns the power summed of
     * all players that have been online
     * the last few days
     *
     * @return A BigDecimal representing the active power
     */
    @NotNull BigDecimal getActivePower();

    /**
     * Returns the maximum power that can be generated by the faction
     * It sums all player's maxpower
     *
     * @return A BigDecimal representing the max reachable power
     */
    @NotNull BigDecimal getTotalMaxPower();

    /**
     * Returns the max power summed of
     * all players that have been online
     * the last few days
     *
     * @return A BigDecimal representing the active max power
     */
    @NotNull BigDecimal getActiveMaxPower();

    /**
     * Returns the power of the player with the given UUID.
     * *
     *
     * @param player The player's UUID
     * @return The player power
     */
    double playerPower(@NotNull UUID player);

    /**
     * Returns the maximum power of the given player.
     *
     * @param player The player's UUID
     * @return The maximum power of the player.
     */
    double maxPlayerPower(@NotNull UUID player);

    /* Relations */

    /**
     * Invite a faction to be an ally. The owner needs to be online
     * *
     *
     * @param faction The faction to invite.
     */
    void inviteAlly(@NotNull Faction<?> faction) throws FactionIsFrozenException,
            FactionOwnerIsOfflineException, CantInviteYourselfException, AlreadyInvitedException;

    /**
     * Removes an invitation to be allied with your faction
     *
     * @param faction The faction to remove the invite from.
     */
    void removeAllyInvite(@NotNull Faction<?> faction);

    /**
     * Returns true if the faction got already invited
     *
     * @param faction The faction you want to check if they are invited.
     * @return if invited
     */
    boolean hasInvited(@NotNull Faction<?> faction);

    /**
     * Returns true if this faction was invited by the given faction.
     *
     * @param faction The faction you want to check if you got invited by.
     * @return If invited
     */
    boolean hasBeenInvitedBy(@NotNull Faction<?> faction);

    /**
     * Returns a stream of all the UUIDs of the invites that have been sent by your faction
     *
     * @return A stream of faction registries
     */
    @NotNull Stream<String> getSentInvites();

    /**
     * Returns a stream of all the invites that the faction has received.
     *
     * @return A stream of faction registries
     */
    @NotNull Stream<String> getReceivedInvites();

    /**
     * Adds an ally to the faction instantly
     *
     * @param faction The faction to add as an ally.
     * @return If the ally was able got added
     */
    boolean addAlly(@NotNull Faction<?> faction) throws FactionIsFrozenException;

    /**
     * Returns true if the given registry is allied with this faction.
     *
     * @param registry The registry of the faction you want to check.
     * @return If allied or not
     */
    boolean isAllied(@NotNull String registry);

    /**
     * Returns true if the player is in an allied faction of this.
     *
     * @param player The player to check.
     * @return If allied or not
     */
    boolean isAllied(@NotNull OfflineFactionPlayer<?> player);

    /**
     * Adds an enemy to the faction.
     *
     * @param faction The faction to add as an enemy.
     * @return If the faction got added as enemy
     */
    boolean addEnemy(@NotNull Faction<?> faction) throws FactionIsFrozenException;

    /**
     * This function returns true if the given registry is an enemy faction.
     *
     * @param registry The registry name of the enemy.
     * @return If enemies or not
     */
    boolean isEnemy(@NotNull String registry);

    /**
     * Returns true if the given player is an enemy of this faction
     *
     * @param player The player to check.
     * @return If the player's faction is an enemy
     */
    boolean isEnemy(@NotNull OfflineFactionPlayer<?> player);

    /**
     * Returns a stream of all the allies of this faction.
     *
     * @return A stream of allied faction registries.
     */
    @NotNull Stream<String> getAllies();

    /**
     * Returns a stream of enemy faction registries.
     *
     * @return A stream of enemy faction registries
     */
    @NotNull Stream<String> getEnemies();

    /**
     * Resets the relation of the given faction to the default relation.
     *
     * @param faction The faction to reset the relation of.
     * @return If successfully reseted.
     */
    boolean resetRelation(@NotNull Faction<?> faction) throws FactionIsFrozenException;

    /* Messages */

    /**
     * BroadcastMessage takes a String as message that should egt sent to everyone
     *
     * @param msg The message to broadcast to all members.
     */
    void broadcastMessage(@NotNull String msg);

    /**
     * Broadcast a translatable message to all players.
     * The translation will be individual for each player based on their selected language
     *
     * @param key The key of the translatable message.
     */
    void broadcastTranslatable(@NotNull String key, Placeholder... parseables);

    /* Claim management */

    /**
     * Returns the claims of this faction.
     *
     * @return A list of claims.
     */
    @NotNull FactionClaims<F> getClaims();

    /* Reports */

    @NotNull FactionReports getReports();

    /* Module management */

    /**
     * Returns the module with the given name, or null if no such module exists
     *
     * @param moduleRegistry The name of the module you want to get.
     * @return A module that is registered to the module registry.
     */
    @Nullable FactionModule<F> getModule(@NotNull String moduleRegistry);

    /**
     * It creates a new instance of the class you pass in, and adds it to the list of modules
     *
     * @param clazz      The class of the module you want to create.
     * @param parameters The values you want to feed into the constructor
     * @param <C>        The module type
     */
    <C extends FactionModule<F>> void createModule(@NotNull Class<C> clazz, Object... parameters)
            throws NoSuchMethodException, InvocationTargetException,
            InstantiationException, IllegalAccessException;


}
