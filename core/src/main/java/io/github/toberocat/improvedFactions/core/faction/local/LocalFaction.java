package io.github.toberocat.improvedFactions.core.faction.local;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.toberocat.improvedFactions.core.event.EventExecutor;
import io.github.toberocat.improvedFactions.core.exceptions.description.DescriptionHasNoLine;
import io.github.toberocat.improvedFactions.core.exceptions.faction.*;
import io.github.toberocat.improvedFactions.core.exceptions.faction.leave.PlayerIsOwnerException;
import io.github.toberocat.improvedFactions.core.exceptions.faction.relation.AlreadyInvitedException;
import io.github.toberocat.improvedFactions.core.exceptions.faction.relation.CantInviteYourselfException;
import io.github.toberocat.improvedFactions.core.exceptions.setting.ErrorParsingSettingException;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.faction.OpenType;
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
import io.github.toberocat.improvedFactions.core.faction.components.report.Report;
import io.github.toberocat.improvedFactions.core.faction.handler.FactionHandler;
import io.github.toberocat.improvedFactions.core.faction.local.module.LocalFactionModule;
import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.handler.ItemHandler;
import io.github.toberocat.improvedFactions.core.item.ItemStack;
import io.github.toberocat.improvedFactions.core.permission.Permission;
import io.github.toberocat.improvedFactions.core.persistent.PersistentHandler;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.player.OfflineFactionPlayer;
import io.github.toberocat.improvedFactions.core.setting.Setting;
import io.github.toberocat.improvedFactions.core.translator.Placeholder;
import io.github.toberocat.improvedFactions.core.utils.DateUtils;
import io.github.toberocat.improvedFactions.core.utils.FileAccess;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joda.time.LocalDateTime;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Stream;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class LocalFaction implements Faction<LocalFaction> {

    @JsonIgnore
    private static final FileAccess access = new FileAccess(ImprovedFactions.api().getLocalDataFolder());

    private final Map<String, LocalFactionModule> modules;
    private final Map<String, String[]> permissions;
    private final Map<UUID, String> memberRanks;
    private final Map<String, String> settingValues;

    private final @NotNull List<String> description;
    private final List<String> enemies;
    private final List<String> allies;
    private final List<String> sentInvites;
    private final List<String> receivedInvites;
    private final List<UUID> members;
    private final List<UUID> banned;
    private final List<Report> factionReports;

    private final @NotNull String createdAt;
    private @NotNull String registry;
    private @NotNull String display;
    private @NotNull String motd;
    private @NotNull String tag;
    private boolean permanent;
    private boolean frozen;
    private @NotNull UUID owner;

    private @NotNull OpenType type;
    private ItemStack factionIcon;

    /**
     * Jackson constructor. Don't use it
     */
    public LocalFaction() {
        registry = "";
        display = "";

        motd = Faction.DEFAULT_MOTD;
        tag = Faction.DEFAULT_TAG;
        permanent = Faction.DEFAULT_PERMANENT;
        frozen = Faction.DEFAULT_FROZEN;

        createdAt = DateUtils.TIME_FORMAT.print(new LocalDateTime());

        type = Faction.DEFAULT_OPEN_TYPE;

        owner = UUID.randomUUID();
        factionIcon = ItemHandler.api().createStack("minecraft:white_banner", display, 1);

        modules = new HashMap<>();
        permissions = new HashMap<>();
        memberRanks = new HashMap<>();
        settingValues = new HashMap<>();

        description = new ArrayList<>();
        enemies = new ArrayList<>();
        allies = new ArrayList<>();
        members = new ArrayList<>();
        banned = new ArrayList<>();
        factionReports = new ArrayList<>();
        sentInvites = new ArrayList<>();
        receivedInvites = new ArrayList<>();
    }

    public LocalFaction(@NotNull LocalFactionDataType dataType) {
        registry = dataType.registry();
        display = dataType.display();
        motd = dataType.motd();
        tag = dataType.tag();
        permanent = dataType.permanent();
        frozen = dataType.frozen();
        createdAt = dataType.createdAt();
        type = dataType.openType();
        owner = dataType.owner();
        description = dataType.description();

        modules = dataType.modules();
        permissions = dataType.permissions();
        settingValues = dataType.settingValues();

        memberRanks = dataType.ranks();
        members = dataType.members();
        banned = dataType.banned();
        allies = dataType.allies();
        enemies = dataType.enemies();
        factionReports = dataType.reports();
        sentInvites = dataType.sentInvites();
        receivedInvites = dataType.receivedInvites();

        try {
            factionIcon = ItemHandler.api().fromBase64(dataType.iconBase64());
        } catch (IOException e) {
            e.printStackTrace();
            factionIcon = ItemHandler.api().createStack("minecraft:white_banner", display, 1);
        }
    }

    public LocalFaction(@NotNull String display, @NotNull FactionPlayer<?> owner)
            throws FactionIsFrozenException,
            PlayerIsAlreadyInFactionException,
            PlayerIsBannedException, FactionAlreadyExistsException, IllegalFactionNamingException {
        this();
        registry = Faction.displayToRegistry(display);

        if (FactionHandler.getAllFactions().anyMatch(x -> x.equals(registry)))
            throw new FactionAlreadyExistsException(this);
        if (!Faction.validNaming(registry))
            throw new IllegalFactionNamingException(this, registry);

        this.display = display;
        this.owner = owner.getUniqueId();
        joinPlayer(owner, (FactionRank) Rank.fromString(FactionOwnerRank.REGISTRY));
    }

    public @NotNull LocalFactionDataType toDataType() {
        return new LocalFactionDataType(registry, display, motd, tag, factionIcon.toBase64(),
                type, frozen, permanent, createdAt, owner, description, banned, members, sentInvites,
                receivedInvites, allies, enemies, factionReports, memberRanks, modules, permissions,
                settingValues);
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
     * Get the icon of this faction
     *
     * @return The icon
     */
    @Override
    public @NotNull ItemStack getIcon() {
        return factionIcon;
    }

    /**
     * Sets the display name of the faction
     *
     * @param display The display name of the faction.
     */
    @Override
    public void renameFaction(@NotNull String display)
            throws FactionIsFrozenException, FactionCantBeRenamedToThisLiteralException {
        if (isFrozen()) throw new FactionIsFrozenException(registry);
        if (!Faction.validNaming(display)) throw new FactionCantBeRenamedToThisLiteralException();
        this.display = Faction.validateDisplay(display);
    }

    /**
     * Set the icon of this faction
     *
     * @throws FactionIsFrozenException Thrown when the faction is frozen and can't be modifed
     */
    @Override
    public void setIcon(@NotNull ItemStack factionIcon) throws FactionIsFrozenException {
        if (isFrozen()) throw new FactionIsFrozenException(registry);
        this.factionIcon = factionIcon;
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
        if (isMember(player)) return getRank(player.getUniqueId());
        if (isAllied(player)) return getRank(player.getUniqueId()).getEquivalent();
        return Rank.fromString(GuestRank.REGISTRY);
    }

    @Override
    public @NotNull Rank getPlayerRank(@NotNull UUID id) {
        if (isMember(id)) return getRank(id);
        return Rank.fromString(GuestRank.REGISTRY);
    }

    /**
     * Returns true if the player is a member of the faction
     *
     * @param player The player to check.
     * @return If the player is in the faction
     */
    @Override
    public boolean isMember(@NotNull OfflineFactionPlayer<?> player) {
        return members.contains(player.getUniqueId());
    }

    @Override
    public boolean isMember(@NotNull UUID player) {
        return members.contains(player);
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
        setRank(player, rank);
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

        setRank(player, (FactionRank) Rank.fromString(FactionOwnerRank.REGISTRY));
        setRank(currentOwner, (FactionRank) Rank.fromString(FactionAdminRank.REGISTRY));

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

            player.getDataContainer().remove(PersistentHandler.FACTION_KEY);
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
        return banned.stream();
    }

    /**
     * Returns a stream of all the members of this faction.
     *
     * @return A stream of UUIDs
     */
    @Override
    public @NotNull Stream<UUID> getMembers() {
        return members.stream();
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

        if (player.inFaction()) throw new PlayerIsAlreadyInFactionException(this, player);
        if (banned.contains(player.getUniqueId())) throw new PlayerIsBannedException(this, player);

        player.getDataContainer().set(PersistentHandler.FACTION_KEY, registry);

        members.add(player.getUniqueId());

        setRank(player, rank);
        broadcastTranslatable(translatable -> translatable
                .getMessages()
                .getFaction()
                .getBroadcast()
                .get("player-join"),
                new Placeholder("{player}", player.getName()),
                new Placeholder("{rank}", player.getMessage(rank.getTitle())));
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

        if (!player.inFaction()) throw new PlayerHasNoFactionException(player);
        player.getDataContainer().remove(PersistentHandler.FACTION_KEY);

        members.remove(player.getUniqueId());
        setRank(player, null);
        return true;
    }

    /**
     * This function kicks a player from the faction
     *
     * @param player The player to kick.
     * @return If the player was able to kicked
     */
    @Override
    public boolean kickPlayer(@NotNull OfflineFactionPlayer<?> player)
            throws FactionIsFrozenException, PlayerNotAMember {
        if (isFrozen()) throw new FactionIsFrozenException(registry);
        if (!isMember(player)) throw new PlayerNotAMember();

        player.getDataContainer().remove(PersistentHandler.FACTION_KEY);
        members.remove(player.getUniqueId());

        player.getDataContainer().remove(PersistentHandler.FACTION_KEY);
        setRank(player, null);
        return true;
    }

    /**
     * This function bans a player.
     *
     * @param player The player to ban.
     * @return If the player got removed from th faction
     */
    @Override
    public boolean banPlayer(@NotNull OfflineFactionPlayer<?> player)
            throws FactionIsFrozenException {
        if (isFrozen()) throw new FactionIsFrozenException(registry);

        banned.add(player.getUniqueId());

        try {
            kickPlayer(player);
        } catch (PlayerNotAMember e) {
            return false;
        }

        setRank(player, null);
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

        banned.remove(player.getUniqueId());
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
        return banned.contains(player.getUniqueId());
    }

    /**
     * Returns the power of the faction
     * It sums all player power
     *
     * @return A BigDecimal representing the total power
     */
    @Override
    public @NotNull BigDecimal getTotalPower() {
        return getMembers().reduce(BigDecimal.ZERO,
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
        return getMembers().reduce(BigDecimal.ZERO,
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

        if (faction.isFrozen()) throw new FactionIsFrozenException(faction.getRegistry());
        if (frozen) throw new FactionIsFrozenException(registry);

        if (faction.getRegistry().equals(registry)) throw new CantInviteYourselfException();
        if (sentInvites.contains(faction.getRegistry())) throw new AlreadyInvitedException(faction);

        FactionPlayer<?> invitedOwner = ImprovedFactions.api().getPlayer(faction.getOwner());
        if (invitedOwner == null) throw new FactionOwnerIsOfflineException(faction);

        if (!(faction instanceof LocalFaction localFaction)) return;

        sentInvites.add(faction.getRegistry());
        localFaction.receivedInvites.add(faction.getRegistry());

    }

    /**
     * Removes an invitation to be allied with your faction
     *
     * @param faction The faction to remove the invite from.
     */
    @Override
    public void removeAllyInvite(@NotNull Faction<?> faction) {
        if (!(faction instanceof LocalFaction localFaction)) return;

        sentInvites.remove(faction.getRegistry());
        localFaction.receivedInvites.remove(faction.getRegistry());
    }

    /**
     * Returns true if the faction got already invited
     *
     * @param faction The faction you want to check if they are invited.
     * @return if invited
     */
    @Override
    public boolean hasInvited(@NotNull Faction<?> faction) {
        return sentInvites.contains(faction.getRegistry());
    }

    /**
     * Returns true if this faction was invited by the given faction.
     *
     * @param faction The faction you want to check if you got invited by.
     * @return If invited
     */
    @Override
    public boolean hasBeenInvitedBy(@NotNull Faction<?> faction) {
        return faction.getReceivedInvites().anyMatch(x -> x.equals(registry));
    }

    /**
     * Returns a stream of all the UUIDs of the invites that have been sent by your faction
     *
     * @return A stream of faction registries
     */
    @Override
    public @NotNull Stream<String> getSentInvites() {
        return sentInvites.stream();
    }

    /**
     * Returns a stream of all the invites that the faction has received.
     *
     * @return A stream of faction registries
     */
    @Override
    public @NotNull Stream<String> getReceivedInvites() {
        return receivedInvites.stream();
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
        allies.add(registry);

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
        return allies.contains(registry);
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
        enemies.add(registry);

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
        return enemies.contains(registry);
    }

    /**
     * Returns true if the given player is an enemy of this faction
     *
     * @param player The player to check.
     * @return If the player's faction is an enemy
     */
    @Override
    public boolean isEnemy(@NotNull OfflineFactionPlayer<?> player) {
        String registry = player.getFactionRegistry();
        return registry != null && enemies.contains(registry);
    }

    /**
     * Returns a stream of all the allies of this faction.
     *
     * @return A stream of allied faction registries.
     */
    @Override
    public @NotNull Stream<String> getAllies() {
        return allies.stream();
    }

    /**
     * Returns a stream of enemy faction registries.
     *
     * @return A stream of enemy faction registries
     */
    @Override
    public @NotNull Stream<String> getEnemies() {
        return enemies.stream();
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

        enemies.remove(faction.getRegistry());
        allies.remove(faction.getRegistry());

        return true;
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
        return new FactionReports() {
            @Override
            public void addReport(@NotNull OfflineFactionPlayer<?> reporter, @NotNull String reason) {
                factionReports.add(new Report(reporter.getUniqueId(), reason));
            }

            @Override
            public @NotNull Stream<Report> getReports() {
                return factionReports.stream();
            }
        };
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
    public @NotNull Stream<String> getPermission(@NotNull Permission permission) {
        return Arrays.stream(permissions.get(permission.label()));
    }

    @Override
    public @NotNull Stream<Permission> listPermissions() {
        return permissions
                .keySet()
                .stream()
                .map(x -> () -> x);
    }

    @Override
    public @NotNull Stream<Permission> listPermissions(@NotNull Rank rank) {
        return permissions.entrySet()
                .stream()
                .filter(e -> Arrays.stream(e.getValue())
                        .anyMatch(x -> x.equals(rank.getRegistry())))
                .map(x -> () -> x.getKey() == null ? "" : x.getKey());
    }

    @Override
    public void setPermission(@NotNull Permission permission, String[] ranks) {
        permissions.put(permission.label(), ranks);
    }

    @Override
    public boolean hasPermission(@NotNull Permission permission, @NotNull Rank rank) {
        if (rank.isAdmin()) return true;
        return Arrays.stream(permissions.computeIfAbsent(permission.label(), l -> new String[0]))
                .anyMatch(x -> x.equals(rank.getRegistry()));
    }

    private void setRank(@NotNull OfflineFactionPlayer<?> player, FactionRank rank) {
        if (rank == null) {
            memberRanks.remove(player.getUniqueId());
            return;
        }
        String old = memberRanks.put(player.getUniqueId(), rank.getRegistry());

        FactionRank oldRank = (FactionRank) Rank.fromString(old == null ? GuestRank.REGISTRY : old);
        EventExecutor.getExecutor().factionMemberRankUpdate(this, player, oldRank, rank);
    }

    private @NotNull Rank getRank(@NotNull UUID player) {
        LocalFactionHandler handler = LocalFactionHandler.getInstance();

        if (handler == null) throw new FactionHandlerNotFound("A local faction " +
                "required a local handler, but didn't find it. " +
                "This is a critical bug and needs to be reported to the dev using discord / github");
        return Rank.fromString(memberRanks.get(player));
    }

    @Override
    public String toString() {
        return registry;
    }

    @Override
    public <T> void setSetting(@NotNull Setting<T> setting, T value) {
        settingValues.put(setting.label(), setting.toSave(value));
    }

    @Override
    public <T> @NotNull T getSetting(@NotNull Setting<T> setting) throws ErrorParsingSettingException {
        String value = settingValues.get(setting.label());
        if (value == null) throw new ErrorParsingSettingException();

        return setting.createFromSave(value);
    }

    @Override
    public @NotNull Stream<Setting<?>> listSettings() {
        return settingValues
                .keySet()
                .stream()
                .map(Setting::getSetting);
    }
}
