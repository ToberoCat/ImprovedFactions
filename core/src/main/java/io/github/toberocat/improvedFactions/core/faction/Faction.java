package io.github.toberocat.improvedFactions.core.faction;

import io.github.toberocat.improvedFactions.core.claims.ClaimHandler;
import io.github.toberocat.improvedFactions.core.exceptions.faction.*;
import io.github.toberocat.improvedFactions.core.exceptions.faction.leave.PlayerIsOwnerException;
import io.github.toberocat.improvedFactions.core.exceptions.faction.relation.AlreadyInvitedException;
import io.github.toberocat.improvedFactions.core.exceptions.faction.relation.CantInviteYourselfException;
import io.github.toberocat.improvedFactions.core.faction.components.Description;
import io.github.toberocat.improvedFactions.core.faction.components.FactionClaims;
import io.github.toberocat.improvedFactions.core.faction.components.FactionModule;
import io.github.toberocat.improvedFactions.core.faction.components.rank.Rank;
import io.github.toberocat.improvedFactions.core.faction.components.rank.members.FactionRank;
import io.github.toberocat.improvedFactions.core.faction.components.report.FactionReports;
import io.github.toberocat.improvedFactions.core.handler.message.MessageHandler;
import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.item.ItemStack;
import io.github.toberocat.improvedFactions.core.permission.Permissions;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.player.OfflineFactionPlayer;
import io.github.toberocat.improvedFactions.core.setting.Settings;
import io.github.toberocat.improvedFactions.core.translator.Placeholder;
import io.github.toberocat.improvedFactions.core.translator.layout.Translatable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public interface Faction<F extends Faction<F>> extends Permissions, Settings {
    /* Static vars */
    int allyId = 0;
    int neutralId = 1;
    int enemyId = 2;

    String REGISTRY_REGEX_PATTERN = ImprovedFactions.api().getConfig()
            .getString("faction.registry-regex", "[^a-zA-Z0-9-_]");
    Pattern REGISTRY_PATTERN = Pattern.compile(REGISTRY_REGEX_PATTERN);

    /* Config values */
    long activeThreshold = ImprovedFactions.api().getConfig().getInt("faction.active-member-threshold", 60);

    /* Default values */

    String DEFAULT_MOTD = ImprovedFactions.api().getConfig().getString("faction.default.motd",
            "Newly created faction");
    String DEFAULT_TAG = ImprovedFactions.api().getConfig().getString("faction.default.tag", "IFF");

    boolean DEFAULT_FROZEN = ImprovedFactions.api().getConfig().getBool("faction.default.frozen");
    boolean DEFAULT_PERMANENT = ImprovedFactions.api().getConfig().getBool("faction.default.permanent");

    long DEFAULT_START_BALANCE = ImprovedFactions.api().getConfig().getLong("faction.default.start-balance");

    OpenType DEFAULT_OPEN_TYPE = ImprovedFactions.api().getConfig()
            .getEnum("faction.default.open-type", OpenType.class).orElse(OpenType.INVITE_ONLY);
    int MAX_FACTION_DISPLAY_LENGTH = ImprovedFactions.api().getConfig().getInt("faction.max-display-length", 10);

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
        return validateDisplay(display)
                .transform(x -> MessageHandler.api().stripColor(x))
                .replaceAll(REGISTRY_PATTERN.pattern(), "");
    }

    static @NotNull String validateDisplay(@NotNull String display) {
        return display.length() <= 10 ? display : display
                .substring(0, MAX_FACTION_DISPLAY_LENGTH)
                .replaceAll(REGISTRY_PATTERN.pattern(), "x");
    }

    /**
     * If the name is 'safezone' or 'warzone', return false. Otherwise, return the result of the
     * ForbiddenChecker.checkName() function.
     * <p>
     * The ForbiddenChecker.checkName() function is defined in the same file,
     * and it's a bit more complicated.
     * It checks if a word has an high similarity to an forbidden one
     *
     * @param name The name of the faction
     * @return If the name is valid
     */
    static boolean invalidNaming(@NotNull String name) {
        return REGISTRY_PATTERN.matcher(name).find() || ClaimHandler.getZones().contains(name);
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
     * Get the icon of this faction
     *
     * @return The icon
     */
    @NotNull ItemStack getIcon();

    /**
     * Sets the display name of the faction
     *
     * @param display The display name of the faction.
     */
    void renameFaction(@NotNull String display) throws FactionIsFrozenException,
            FactionCantBeRenamedToThisLiteralException;

    /**
     * Set the icon of this faction
     *
     * @param factionIcon The new icon of this faction
     * @throws FactionIsFrozenException Thrown when the faction is frozen and can't be modifed
     */
    void setIcon(@NotNull ItemStack factionIcon) throws FactionIsFrozenException;

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
     * @param permanent If true, the faction will be stored permanently.
     *                  If false, the faction needs to be
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
     * Gets the rank of the specified sender.
     *
     * @param player The sender to get the rank of.
     * @return The rank of the sender.
     */
    @NotNull Rank getPlayerRank(@NotNull OfflineFactionPlayer player);

    /**
     * Gets the rank of the specified sender
     *
     * @param id The sender's uuid
     * @return The rank of the sender
     */
    @NotNull Rank getPlayerRank(@NotNull UUID id);

    /**
     * Returns true if the sender is a member of the faction
     *
     * @param player The sender to check.
     * @return If the sender is in the faction
     */
    boolean isMember(@NotNull OfflineFactionPlayer player);


    /**
     * Returns true if the sender is a member of the faction
     *
     * @param player The sender to check.
     * @return If the sender is in the faction
     */
    boolean isMember(@NotNull UUID player);

    /**
     * Changes the rank of the specified sender to the specified rank
     *
     * @param player The sender you want to change the rank of.
     * @param rank   The rank you want to change the sender to.
     */
    void changeRank(@NotNull OfflineFactionPlayer player, @NotNull FactionRank rank)
            throws FactionIsFrozenException;

    /* Faction management */

    /**
     * Transfer ownership of the faction to the specified sender.
     *
     * @param player The sender who will be the new owner of the faction.
     */
    void transferOwnership(@NotNull FactionPlayer player) throws FactionIsFrozenException;

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
    default Stream<OfflineFactionPlayer> getPlayers() {
        return getMembers()
                .map(x -> ImprovedFactions.api().getOfflinePlayer(x));
    }

    default Stream<OfflineFactionPlayer> getActiveMembers() {
        return getPlayers().filter(x -> TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() -
                x.getLastPlayed()) <= activeThreshold);
    }

    default Stream<OfflineFactionPlayer> getOnlineMembers() {
        return getPlayers().filter(OfflineFactionPlayer::isOnline);
    }


    /**
     * joinPlayer joins a sender to the faction
     *
     * @param player The sender to join the game.
     * @return If it was able to join
     */
    boolean joinPlayer(@NotNull FactionPlayer player)
            throws FactionIsFrozenException, PlayerIsAlreadyInFactionException,
            PlayerIsBannedException;

    /**
     * Join a sender in a faction to a rank.
     *
     * @param player The sender to join the faction.
     * @param rank   The rank that the sender will be joining as.
     * @return If the sender was able to join
     */
    boolean joinPlayer(@NotNull FactionPlayer player, @NotNull FactionRank rank)
            throws FactionIsFrozenException, PlayerIsAlreadyInFactionException,
            PlayerIsBannedException;

    /**
     * Removes a sender from the faction
     *
     * @param player The sender to leave the faction.
     * @return If the sender was able to leave
     */
    boolean leavePlayer(@NotNull FactionPlayer player)
            throws FactionIsFrozenException, PlayerIsOwnerException,
            PlayerHasNoFactionException;

    /**
     * This function kicks a sender from the faction
     *
     * @param player The sender to kick.
     * @return If the sender was able to kicked
     */
    boolean kickPlayer(@NotNull OfflineFactionPlayer player) throws FactionIsFrozenException,
            PlayerNotAMember;

    /**
     * This function bans a sender.
     *
     * @param player The sender to ban.
     * @return If the sender was able to get banned
     */
    boolean banPlayer(@NotNull OfflineFactionPlayer player) throws FactionIsFrozenException, PlayerNotAMember;

    /**
     * Pardon a sender from the ban list.
     *
     * @param player The sender to pardon.
     * @return If the sender was able to be pardoned
     */
    boolean pardonPlayer(@NotNull OfflineFactionPlayer player) throws FactionIsFrozenException;

    /**
     * Returns true if the sender is banned, false otherwise.
     *
     * @param player The sender to check
     * @return If banned or not
     */
    boolean isBanned(@NotNull OfflineFactionPlayer player);


    /* Power management */

    /**
     * Returns the power of the faction
     * It sums all sender power
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
     * It sums all sender's maxpower
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
     * Returns true if the sender is in an allied faction of this.
     *
     * @param player The sender to check.
     * @return If allied or not
     */
    boolean isAllied(@NotNull OfflineFactionPlayer player);

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
     * Returns true if the given sender is an enemy of this faction
     *
     * @param player The sender to check.
     * @return If the sender's faction is an enemy
     */
    boolean isEnemy(@NotNull OfflineFactionPlayer player);

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
    default void broadcastMessage(@NotNull String msg) {
        ImprovedFactions api = ImprovedFactions.api();
        getMembers()
                .map(api::getOfflinePlayer)
                .filter(Objects::nonNull)
                .forEach(x -> x.sendMessage(msg));
    }

    /**
     * Broadcast a translatable message to all players.
     * The translation will be individual for each sender based on their selected language
     *
     * @param query The key of the translatable message.
     */
    default void broadcastTranslatable(@NotNull Function<Translatable,
            @NotNull String> query,
            Placeholder... parseables) {
        ImprovedFactions api = ImprovedFactions.api();
        getMembers()
                .map(api::getOfflinePlayer)
                .filter(Objects::nonNull)
                .forEach(x -> x.sendMessage(query, parseables));
    }

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
