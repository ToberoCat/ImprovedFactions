package io.github.toberocat.improvedFactions.core.faction.local;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.toberocat.improvedFactions.core.event.EventExecutor;
import io.github.toberocat.improvedFactions.core.exceptions.description.DescriptionHasNoLine;
import io.github.toberocat.improvedFactions.core.exceptions.faction.*;
import io.github.toberocat.improvedFactions.core.exceptions.faction.leave.PlayerIsOwnerException;
import io.github.toberocat.improvedFactions.core.faction.components.Description;
import io.github.toberocat.improvedFactions.core.faction.components.FactionClaims;
import io.github.toberocat.improvedFactions.core.faction.components.FactionModule;
import io.github.toberocat.improvedFactions.core.faction.components.rank.GuestRank;
import io.github.toberocat.improvedFactions.core.faction.components.rank.Rank;
import io.github.toberocat.improvedFactions.core.faction.components.rank.members.FactionAdminRank;
import io.github.toberocat.improvedFactions.core.faction.components.rank.members.FactionMemberRank;
import io.github.toberocat.improvedFactions.core.faction.components.rank.members.FactionOwnerRank;
import io.github.toberocat.improvedFactions.core.faction.components.rank.members.FactionRank;
import io.github.toberocat.improvedFactions.core.faction.components.report.FactionReports;
import io.github.toberocat.improvedFactions.core.faction.local.module.LocalFactionModule;
import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.player.OfflineFactionPlayer;
import io.github.toberocat.improvedFactions.core.translator.Placeholder;
import io.github.toberocat.improvedFactions.core.translator.layout.Translatable;
import io.github.toberocat.improvedFactions.core.utils.DateUtils;
import io.github.toberocat.improvedFactions.core.utils.FileAccess;
import io.github.toberocat.improvedFactions.core.utils.ReturnConsumer;
import io.github.toberocat.improvedFactions.core.exceptions.faction.relation.AlreadyInvitedException;
import io.github.toberocat.improvedFactions.core.exceptions.faction.relation.CantInviteYourselfException;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.faction.OpenType;
import io.github.toberocat.improvedFactions.core.faction.handler.FactionHandler;
import io.github.toberocat.improvedFactions.core.faction.local.managers.FactionMemberManager;
import io.github.toberocat.improvedFactions.core.faction.local.managers.FactionPerm;
import io.github.toberocat.improvedFactions.core.faction.local.managers.RelationManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joda.time.LocalDateTime;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Stream;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class LocalFaction implements Faction<LocalFaction> {

    @JsonIgnore
    private static final FileAccess access = new FileAccess(ImprovedFactions.api().getLocalFolder());
    private @NotNull
    final String createdAt;
    private @NotNull
    final List<String> description;
    private final @NotNull FactionPerm factionPermissions;
    private final @NotNull FactionMemberManager factionMembers;
    private final @NotNull RelationManager relationManager;
    private final Map<String, LocalFactionModule> modules = new LinkedHashMap<>();
    private @NotNull String registry;
    private @NotNull String display;
    private @NotNull String motd;
    private @NotNull String tag;
    private boolean permanent;
    private boolean frozen;
    private @NotNull UUID owner;
    private @NotNull OpenType type;

    /**
     * Jackson constructor. Don't use it
     */
    public LocalFaction() {
        this.registry = "";
        this.display = "";

        this.motd = DEFAULT_MOTD;
        this.tag = DEFAULT_TAG;
        this.permanent = DEFAULT_PERMANENT;
        this.frozen = DEFAULT_FROZEN;

        this.createdAt = DateUtils.TIME_FORMAT.print(new LocalDateTime());

        this.type = DEFAULT_OPEN_TYPE;

        this.owner = UUID.randomUUID();

        this.description = new ArrayList<>();
        this.factionPermissions = new FactionPerm(this);
        this.factionMembers = new FactionMemberManager(this);
        this.relationManager = new RelationManager(this);
    }

    public LocalFaction(@NotNull String display, @NotNull FactionPlayer<?> owner) {
        this();
        this.registry = Faction.displayToRegistry(display);
        this.display = display;
        this.owner = owner.getUniqueId();
    }

    /**
     * Returns the registry of the faction.
     *
     * @return The registry.
     */
    @Override
    public @NotNull String getRegistry() {
        return registry;
    }

    /**
     * Returns the display name of the faction.
     *
     * @return The display name. Can contain:
     * - Color (§a, etc.)
     * - Special characters
     */
    @Override
    public @NotNull String getDisplay() {
        return display;
    }

    /**
     * Sets the display name of the faction
     *
     * @param display The display name of the faction.
     */
    @Override
    public void setDisplay(@NotNull String display) throws FactionIsFrozenException {
        if (isFrozen()) throw new FactionIsFrozenException(registry);
        this.display = display;
    }

    /**
     * Get the faction color
     *
     * @return The color of the faction.
     */
    @Override
    public int getColor() {
        return 0; //Todo: Add getting the color of a faction using settings
    }

    /**
     * Returns the message of the day.
     *
     * @return The message of the day.
     */
    @Override
    public @NotNull String getMotd() {
        return motd;
    }

    /**
     * Sets the faction's MOTD
     *
     * @param motd The message of the day.
     */
    @Override
    public void setMotd(@NotNull String motd) throws FactionIsFrozenException {
        if (isFrozen()) throw new FactionIsFrozenException(registry);
        this.motd = motd;
    }

    /**
     * Returns the tag of this faction
     *
     * @return A string, whose max length is limited to the tag length
     */
    @Override
    public @NotNull String getTag() {
        return tag;
    }

    /**
     * Sets the tag of the faction
     *
     * @param tag The tag to set.
     */
    @Override
    public void setTag(@NotNull String tag) throws FactionIsFrozenException {
        if (isFrozen()) throw new FactionIsFrozenException(registry);
        this.tag = tag;
    }

    /**
     * Returns an instance of the anonymous description class
     *
     * @return The description object
     */
    @Override
    public @NotNull Description getDescription() {
        return new Description() {
            /**
             * Returns a stream of lines from the description.
             *
             * @return All lines of the description.
             */
            @Override
            public @NotNull Stream<String> getLines() {
                return description.stream();
            }

            @Override
            public @NotNull String getLine(int line) throws DescriptionHasNoLine {
                if (line >= description.size()) throw new DescriptionHasNoLine(registry, line);
                return description.get(line);
            }

            @Override
            public void setLine(int line, @NotNull String content)
                    throws FactionIsFrozenException {
                if (isFrozen()) throw new FactionIsFrozenException(registry);

                if (line >= description.size()) description.add(content);
                else description.set(line, content);

            }

            @Override
            public boolean hasLine(int line) {
                return line < description.size();
            }

            @Override
            public int getLastLine() {
                return description.size() - 1;
            }
        };
    }

    /**
     * Returns the date and time when the faction was created.
     *
     * @return A string
     */
    @Override
    public @NotNull String getCreatedAt() {
        return createdAt;
    }

    /**
     * Returns the type of the faction.
     *
     * @return The type
     */
    @Override
    public @NotNull OpenType getType() {
        return type;
    }

    /**
     * Sets the type of the faction
     *
     * @param type The type
     */
    @Override
    public void setType(@NotNull OpenType type) throws FactionIsFrozenException {
        if (isFrozen()) throw new FactionIsFrozenException(registry);
        this.type = type;
    }

    /**
     * Returns the owner of this faction.
     *
     * @return The owner of the faction.
     */
    @Override
    public @NotNull UUID getOwner() {
        return owner;
    }

    /**
     * Returns true if the faction is permanent.
     * A permanent faction can exist without members in it
     *
     * @return If permanent
     */
    @Override
    public boolean isPermanent() {
        return permanent;
    }

    /**
     * Sets whether the faction is permanent or not
     *
     * @param permanent If true, the faction will be stored permanently.
     *                  If false, the faction needs to be
     *                  deleted when the owner tries to leave
     */
    @Override
    public void setPermanent(boolean permanent) {
        this.permanent = permanent;
    }

    /**
     * Returns true if the faction is frozen, otherwise returns false.
     *
     * @return If frozen
     */
    @Override
    public boolean isFrozen() {
        return frozen;
    }

    /**
     * Sets the frozen state of the faction
     * Frozen factions can't change their current state
     *
     * @param frozen true if the faction is frozen, false if not
     */
    @Override
    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    /**
     * Gets the rank of the specified player.
     *
     * @param player The player to get the rank of.
     * @return The rank of the player.
     */
    @Override
    public @NotNull Rank getPlayerRank(@NotNull OfflineFactionPlayer<?> player) {
        try {
            return factionPermissions.getPlayerRank(player);
        } catch (FactionNotInStorage e) {
            e.printStackTrace();
            return Rank.fromString(GuestRank.REGISTRY);
        }
    }

    /**
     * Returns true if the player is a member of the faction
     *
     * @param player The player to check.
     * @return If the player is in the faction
     */
    @Override
    public boolean isMember(@NotNull OfflineFactionPlayer<?> player) {
        return factionMembers.getMembers().contains(player.getUniqueId());
    }

    /**
     * Changes the rank of the specified player to the specified rank
     *
     * @param player The player you want to change the rank of.
     * @param rank   The rank you want to change the player to.
     */
    @Override
    public void changeRank(@NotNull OfflineFactionPlayer<?> player, @NotNull FactionRank rank)
            throws FactionIsFrozenException {
        if (isFrozen()) throw new FactionIsFrozenException(registry);
        factionPermissions.setRank(player, rank);
    }

    /**
     * Transfer ownership of the faction to the specified player.
     *
     * @param player The player who will be the new owner of the faction.
     */
    @Override
    public void transferOwnership(@NotNull FactionPlayer<?> player) throws FactionIsFrozenException {
        if (isFrozen()) throw new FactionIsFrozenException(registry);
        OfflineFactionPlayer<?> currentOwner = ImprovedFactions.api().getOfflinePlayer(owner);
        if (currentOwner == null) return;

        factionPermissions.setRank(player, (FactionRank) Rank.fromString(FactionOwnerRank.REGISTRY));
        factionPermissions.setRank(currentOwner, (FactionRank) Rank.fromString(FactionAdminRank.REGISTRY));

        EventExecutor.getExecutor().transferOwnership(this, currentOwner, player);
    }

    /**
     * Deletes the faction
     */
    @Override
    public void deleteFaction() throws FactionIsFrozenException {
        if (isFrozen()) throw new FactionIsFrozenException(registry);

        getMembers().forEach(uuid -> {
            OfflineFactionPlayer<?> player = ImprovedFactions.api().getOfflinePlayer(uuid);
            if (player == null) return;

            try {
                kickPlayer(player);
            } catch (FactionIsFrozenException ignored) {
            }
        });

        getClaims().unclaimAll();


        FactionHandler.deleteCache(registry);
        access.delete(FileAccess.FACTION_FOLDER, registry + ".json");
    }

    /**
     * Returns a stream of all banned players.
     *
     * @return A stream of UUIDs
     */
    @Override
    public @NotNull Stream<UUID> getBanned() {
        return factionMembers.getBanned().stream();
    }

    /**
     * Returns a stream of all the members of this faction.
     *
     * @return A stream of UUIDs
     */
    @Override
    public @NotNull Stream<UUID> getMembers() {
        return factionMembers.getMembers().stream();
    }

    /**
     * joinPlayer joins a player to the faction
     *
     * @param player The player to join the game.
     * @return If it was able to join
     */
    @Override
    public boolean joinPlayer(@NotNull FactionPlayer<?> player) throws FactionIsFrozenException,
            PlayerIsAlreadyInFactionException, PlayerIsBannedException {
        return joinPlayer(player, (FactionRank) Rank.fromString(FactionMemberRank.REGISTRY));
    }

    /**
     * Join a player in a faction to a rank.
     *
     * @param player The player to join the faction.
     * @param rank   The rank that the player will be joining as.
     * @return If the player was able to join
     */
    @Override
    public boolean joinPlayer(@NotNull FactionPlayer<?> player, @NotNull FactionRank rank)
            throws FactionIsFrozenException, PlayerIsAlreadyInFactionException, PlayerIsBannedException {
        if (isFrozen()) throw new FactionIsFrozenException(registry);

        factionMembers.join(player);
        factionPermissions.setRank(player, rank);
        return true;
    }

    /**
     * Removes a player from the faction
     *
     * @param player The player to leave the faction.
     * @return If the player was able to leave
     */
    @Override
    public boolean leavePlayer(@NotNull FactionPlayer<?> player)
            throws FactionIsFrozenException, PlayerIsOwnerException, PlayerHasNoFactionException {
        if (isFrozen()) throw new FactionIsFrozenException(registry);
        if (!isPermanent() && getPlayerRank(player).getRegistry().equals(FactionOwnerRank.REGISTRY))
            throw new PlayerIsOwnerException(this, player);

        factionMembers.leave(player);
        factionPermissions.setRank(player, null);
        return true;
    }

    /**
     * This function kicks a player from the faction
     *
     * @param player The player to kick.
     * @return If the player was able to kicked
     */
    @Override
    public boolean kickPlayer(@NotNull OfflineFactionPlayer<?> player) throws FactionIsFrozenException {
        if (isFrozen()) throw new FactionIsFrozenException(registry);

        factionMembers.kick(player);
        factionPermissions.setRank(player, null);
        return true;
    }

    /**
     * This function bans a player.
     *
     * @param player The player to ban.
     * @return If the player was able to get banned
     */
    @Override
    public boolean banPlayer(@NotNull OfflineFactionPlayer<?> player)
            throws FactionIsFrozenException {
        if (isFrozen()) throw new FactionIsFrozenException(registry);

        factionMembers.ban(player);
        factionPermissions.setRank(player, null);
        return true;
    }

    /**
     * Pardon a player from the ban list.
     *
     * @param player The player to pardon.
     * @return If the player was able to be pardoned
     */
    @Override
    public boolean pardonPlayer(@NotNull OfflineFactionPlayer<?> player)
            throws FactionIsFrozenException {
        if (isFrozen()) throw new FactionIsFrozenException(registry);

        factionMembers.pardon(player);
        return true;
    }

    /**
     * Returns true if the player is banned, false otherwise.
     *
     * @param player The player to check
     * @return If banned or not
     */
    @Override
    public boolean isBanned(@NotNull OfflineFactionPlayer<?> player) {
        return factionMembers.getBanned().contains(player.getUniqueId());
    }

    /**
     * Returns the power of the faction
     * It sums all player power
     *
     * @return A BigDecimal representing the total power
     */
    @Override
    public @NotNull BigDecimal getTotalPower() {
        return factionMembers.getMembers().stream().reduce(BigDecimal.ZERO,
                (bigDecimal, uuid) -> bigDecimal.add(BigDecimal.valueOf(playerPower(uuid))),
                BigDecimal::add);
    }

    /**
     * Returns the power summed of
     * all players that have been online
     * the last few days
     *
     * @return A BigDecimal representing the active power
     */
    @Override
    public @NotNull BigDecimal getActivePower() {
        return getActiveMembers()
                .reduce(BigDecimal.ZERO,
                        (bigDecimal, player) ->
                                bigDecimal.add(BigDecimal
                                        .valueOf(playerPower(player.getUniqueId()))),
                        BigDecimal::add);
    }

    /**
     * Returns the maximum power that can be generated by the faction
     * It sums all player's maxpower
     *
     * @return A BigDecimal representing the max reachable power
     */
    @Override
    public @NotNull BigDecimal getTotalMaxPower() {
        return factionMembers.getMembers().stream().reduce(BigDecimal.ZERO,
                (bigDecimal, uuid) -> bigDecimal.add(BigDecimal.valueOf(maxPlayerPower(uuid))),
                BigDecimal::add
        );
    }

    /**
     * Returns the max power summed of
     * all players that have been online
     * the last few days
     *
     * @return A BigDecimal representing the active max power
     */
    @Override
    public @NotNull BigDecimal getActiveMaxPower() {
        return getActiveMembers()
                .reduce(BigDecimal.ZERO,
                        (bigDecimal, x) ->
                                bigDecimal.add(BigDecimal.valueOf(maxPlayerPower(x.getUniqueId()))),
                        BigDecimal::add
                );
    }

    /**
     * Returns the power of the player with the given UUID.
     * *
     *
     * @param player The player's UUID
     * @return The player power
     */
    @Override
    public double playerPower(@NotNull UUID player) {
        // ToDo: Get player power
        return 0;
    }

    /**
     * Returns the maximum power of the given player.
     *
     * @param player The player's UUID
     * @return The maximum power of the player.
     */
    @Override
    public double maxPlayerPower(@NotNull UUID player) {
        // ToDo: Get player max power
        return 0;
    }

    /**
     * Invite a faction to be an ally. The owner needs to be online
     * *
     *
     * @param faction The faction to invite.
     */
    @Override
    public void inviteAlly(@NotNull Faction<?> faction) throws FactionIsFrozenException,
            FactionOwnerIsOfflineException, CantInviteYourselfException, AlreadyInvitedException {
        // ToDo: invite ally
    }

    /**
     * Removes an invitation to be allied with your faction
     *
     * @param faction The faction to remove the invite from.
     */
    @Override
    public void removeAllyInvite(@NotNull Faction<?> faction) {
        // ToDo: remove ally invite ally
    }

    /**
     * Returns true if the faction got already invited
     *
     * @param faction The faction you want to check if they are invited.
     * @return if invited
     */
    @Override
    public boolean hasInvited(@NotNull Faction<?> faction) {
        // ToDo: get if ally has been invited
        return false;
    }

    /**
     * Returns true if this faction was invited by the given faction.
     *
     * @param faction The faction you want to check if you got invited by.
     * @return If invited
     */
    @Override
    public boolean hasBeenInvitedBy(@NotNull Faction<?> faction) {
        // ToDo: get if faction has been invited by other faction
        return false;
    }

    /**
     * Returns a stream of all the UUIDs of the invites that have been sent by your faction
     *
     * @return A stream of faction registries
     */
    @Override
    public @NotNull Stream<String> getSentInvites() {
        // ToDo: get all invites the faction sent
        return null;
    }

    /**
     * Returns a stream of all the invites that the faction has received.
     *
     * @return A stream of faction registries
     */
    @Override
    public @NotNull Stream<String> getReceivedInvites() {
        // ToDo: get all invites the faction received
        return null;
    }

    /**
     * Adds an ally to the faction
     *
     * @param faction The faction to add as an ally.
     * @return If the ally was able got added
     */
    @Override
    public boolean addAlly(@NotNull Faction<?> faction)
            throws FactionIsFrozenException {
        if (isFrozen()) throw new FactionIsFrozenException(registry);

        String registry = faction.getRegistry();
        if (isEnemy(registry) || isAllied(registry)) return false;
        relationManager.getAllies().add(registry);

        return true;
    }

    /**
     * Returns true if the given registry is allied with this faction.
     *
     * @param registry The registry of the faction you want to check.
     * @return If allied or not
     */
    @Override
    public boolean isAllied(@NotNull String registry) {
        return relationManager.getAllies().contains(registry);
    }

    /**
     * Returns true if the player is in an allied faction of this.
     *
     * @param player The player to check.
     * @return If allied or not
     */
    @Override
    public boolean isAllied(@NotNull OfflineFactionPlayer<?> player) {
        String registry = player.getFactionRegistry();
        return registry != null && isAllied(registry);
    }

    /**
     * Adds an enemy to the faction.
     *
     * @param faction The faction to add as an enemy.
     * @return If the faction got added as enemy
     */
    @Override
    public boolean addEnemy(@NotNull Faction<?> faction)
            throws FactionIsFrozenException {
        if (isFrozen()) throw new FactionIsFrozenException(registry);

        String registry = faction.getRegistry();
        if (isEnemy(registry) || isAllied(registry)) return false;
        relationManager.getEnemies().add(registry);

        return true;
    }

    /**
     * This function returns true if the given registry is an enemy faction.
     *
     * @param registry The registry name of the enemy.
     * @return If enemies or not
     */
    @Override
    public boolean isEnemy(@NotNull String registry) {
        return relationManager.getEnemies().contains(registry);
    }

    /**
     * Returns true if the given player is an enemy of this faction
     *
     * @param player The player to check.
     * @return If the player's faction is an enemy
     */
    @Override
    public boolean isEnemy(@NotNull OfflineFactionPlayer<?> player) {
        // ToDo: Get if player is enemy
        return false;
    }

    /**
     * Returns a stream of all the allies of this faction.
     *
     * @return A stream of allied faction registries.
     */
    @Override
    public @NotNull Stream<String> getAllies() {
        return relationManager.getAllies().stream();
    }

    /**
     * Returns a stream of enemy faction registries.
     *
     * @return A stream of enemy faction registries
     */
    @Override
    public @NotNull Stream<String> getEnemies() {
        return relationManager.getEnemies().stream();
    }

    /**
     * Resets the relation of the given faction to the default relation.
     *
     * @param faction The faction to reset the relation of.
     * @return If successfully reseted.
     */
    @Override
    public boolean resetRelation(@NotNull Faction<?> faction)
            throws FactionIsFrozenException {
        if (isFrozen()) throw new FactionIsFrozenException(registry);

        relationManager.getEnemies().remove(faction.getRegistry());
        relationManager.getAllies().remove(faction.getRegistry());

        return true;
    }

    /**
     * BroadcastMessage takes a String as message that should egt sent to everyone
     *
     * @param msg The message to broadcast to all members.
     */
    @Override
    public void broadcastMessage(@NotNull String msg) {
        // ToDo: Broadcast msg
    }

    /**
     * Broadcast a translatable message to all players.
     * The translation will be individual for each player based on their selected language
     *
     * @param query        The key of the translatable message.
     * @param parseables
     */
    @Override
    public void broadcastTranslatable(@NotNull ReturnConsumer<Translatable, String> query, Placeholder... parseables) {
        // ToDo: Broadcast msg
    }

    /**
     * Returns the claims of this faction.
     *
     * @return A list of claims.
     */
    @Override
    public @NotNull FactionClaims<LocalFaction> getClaims() {
        return FactionClaims.createClaims(this);
    }


    @Override
    public @NotNull FactionReports getReports() {
        // ToDo: Get reports
        return null;
    }

    /**
     * Returns the module with the given name, or null if no such module exists
     *
     * @param moduleRegistry The name of the module you want to get.
     * @return A module that is registered to the module registry.
     */
    @Override
    public @Nullable LocalFactionModule getModule(@NotNull String moduleRegistry) {
        return modules.get(moduleRegistry);
    }

    /**
     * It creates a new instance of the class you pass in, and adds it to the list of modules
     *
     * @param clazz      The class of the module you want to create.
     * @param parameters The values you want to feed into the constructor
     */
    @Override
    public <C extends FactionModule<LocalFaction>> void createModule(@NotNull Class<C> clazz,
                                                                     Object... parameters)
            throws NoSuchMethodException, InvocationTargetException,
            InstantiationException, IllegalAccessException {

        LinkedList<Class<?>> classes = new LinkedList<>(Arrays.stream(parameters)
                .filter(Objects::nonNull)
                .map(Object::getClass)
                .toList());

        FactionModule<LocalFaction> module = clazz.getConstructor(classes.toArray(Class[]::new))
                .newInstance(parameters);
        modules.put(module.registry(), (LocalFactionModule) module);
    }

    @Override
    public String toString() {
        return registry;
    }
}
