package io.github.toberocat.core.factions.local;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.toberocat.MainIF;
import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.FactionManager;
import io.github.toberocat.core.factions.OpenType;
import io.github.toberocat.core.factions.components.Description;
import io.github.toberocat.core.factions.components.FactionClaims;
import io.github.toberocat.core.factions.components.FactionModule;
import io.github.toberocat.core.factions.components.rank.Rank;
import io.github.toberocat.core.factions.components.rank.members.AdminRank;
import io.github.toberocat.core.factions.components.rank.members.FactionRank;
import io.github.toberocat.core.factions.components.rank.members.MemberRank;
import io.github.toberocat.core.factions.components.rank.members.OwnerRank;
import io.github.toberocat.core.factions.handler.FactionHandler;
import io.github.toberocat.core.factions.local.managers.FactionMemberManager;
import io.github.toberocat.core.factions.local.managers.FactionPerm;
import io.github.toberocat.core.factions.local.managers.RelationManager;
import io.github.toberocat.core.factions.local.module.LocalFactionModule;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.data.Table;
import io.github.toberocat.core.utility.data.access.FileAccess;
import io.github.toberocat.core.utility.date.DateCore;
import io.github.toberocat.core.utility.events.faction.*;
import io.github.toberocat.core.utility.exceptions.DescriptionHasNoLine;
import io.github.toberocat.core.utility.exceptions.faction.FactionIsFrozenException;
import io.github.toberocat.core.utility.exceptions.setting.SettingNotFoundException;
import io.github.toberocat.core.utility.settings.type.RankSetting;
import io.github.toberocat.core.utility.settings.type.Setting;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
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
    private static final FileAccess access = FileAccess.accessPipeline(FileAccess.class);

    private @NotNull String registry;
    private @NotNull String display;
    private @NotNull String motd;
    private @NotNull String tag;
    private @NotNull String createdAt;

    private boolean permanent;
    private boolean frozen;

    private @NotNull UUID owner;

    private @NotNull OpenType type;

    private @NotNull List<String> description;

    private final @NotNull FactionPerm factionPermissions;
    private final @NotNull FactionMemberManager factionMembers;
    private final @NotNull RelationManager relationManager;

    private Map<String, LocalFactionModule> modules = new LinkedHashMap<>();

    ;
    /**
     * Jackson constructor. Don't use it
     */
    public LocalFaction() {
        FileConfiguration config = MainIF.config();

        this.registry = "";
        this.display = "";

        this.motd = config.getString("faction.default.motd", "Improved faction");
        this.tag = config.getString("faction.default.tag", "IFF");
        this.permanent = config.getBoolean("faction.default.permanent");
        this.frozen = config.getBoolean("faction.default.frozen");

        this.createdAt = DateCore.TIME_FORMAT.print(new LocalDateTime());

        this.type = OpenType.INVITE_ONLY;

        this.owner = UUID.randomUUID();

        this.description = new ArrayList<>();
        this.factionPermissions = new FactionPerm(this);
        this.factionMembers = new FactionMemberManager(this);
        this.relationManager = new RelationManager(this);
    }

    public LocalFaction(@NotNull String display, @NotNull Player owner) {
        this();
        this.registry = Faction.displayToRegistry(display);
        this.display = display;
        this.owner = owner.getUniqueId();
    }

    /**
     * It creates populates the faction by loading everything it needs from storage
     *
     * @param loadRegistry The registry of the faction to load.
     */
    @Override
    public void createFromStorage(@NotNull String loadRegistry) {
        throw new NotImplementedException("A local faction can't get created using Faction#createFromStorage(String)");
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
            public void setLine(int line, @NotNull String content) throws FactionIsFrozenException {
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
     * @param permanent If true, the faction will be stored permanently. If false, the faction needs to be
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
    public @NotNull Rank getPlayerRank(@NotNull OfflinePlayer player) {
        return factionPermissions.getPlayerRank(player);
    }

    /**
     * Gets the permission setting for the specified permission
     *
     * @param permission The permission you want to get the setting for.
     * @return A RankSetting instance.
     */
    @Override
    public @NotNull RankSetting getPermission(@NotNull String permission) throws SettingNotFoundException {
        if (!factionPermissions.getRankSetting().containsKey(permission))
            throw new SettingNotFoundException(permission);
        return factionPermissions.getRankSetting().get(permission);
    }

    /**
     * Returns whether the player has the given permission
     *
     * @param player     The player to check the permission for.
     * @param permission The permission to check for.
     * @return If the permission is allowed for the specified player
     */
    @Override
    public boolean hasPermission(@NotNull OfflinePlayer player, @NotNull String permission) throws SettingNotFoundException {
        RankSetting setting = getPermission(permission);
        return setting.hasPermission(getPlayerRank(player));
    }

    /**
     * Returns true if the player is a member of the faction
     *
     * @param player The player to check.
     * @return If the player is in the faction
     */
    @Override
    public boolean isMember(@NotNull OfflinePlayer player) {
        return factionMembers.getMembers().contains(player.getUniqueId());
    }

    /**
     * Changes the rank of the specified player to the specified rank
     *
     * @param player The player you want to change the rank of.
     * @param rank   The rank you want to change the player to.
     */
    @Override
    public void changeRank(@NotNull OfflinePlayer player, @NotNull FactionRank rank)
            throws FactionIsFrozenException {
        if (isFrozen()) throw new FactionIsFrozenException(registry);
        factionPermissions.setRank(player, rank.getRegistryName());
    }

    /**
     * Transfer ownership of the faction to the specified player.
     *
     * @param player The player who will be the new owner of the faction.
     */
    @Override
    public void transferOwnership(@NotNull Player player) throws FactionIsFrozenException {
        if (isFrozen()) throw new FactionIsFrozenException(registry);
        OfflinePlayer currentOwner = Bukkit.getOfflinePlayer(owner);

        factionPermissions.setRank(player, OwnerRank.registry);
        factionPermissions.setRank(currentOwner, AdminRank.registry);

        Bukkit.getServer().getPluginManager().callEvent(new FactionTransferOwnershipEvent(this,
                currentOwner, player));
    }

    /**
     * Deletes the faction
     */
    @Override
    public void deleteFaction() throws FactionIsFrozenException {
        if (isFrozen()) throw new FactionIsFrozenException(registry);
        if (!Utility.callEvent(new FactionDeleteEvent(this))) return;

        getMembers().forEach(uuid -> Utility.run(() -> kickPlayer(Bukkit.getOfflinePlayer(uuid))));

        getClaims().unclaimAll();


        FactionHandler.deleteCache(registry);
        access.delete(Table.FACTIONS, registry);
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
    public boolean joinPlayer(@NotNull Player player) throws FactionIsFrozenException {
        return joinPlayer(player, Rank.fromString(MemberRank.registry));
    }

    /**
     * Join a player in a faction to a rank.
     *
     * @param player The player to join the faction.
     * @param rank   The rank that the player will be joining as.
     * @return If the player was able to join
     */
    @Override
    public boolean joinPlayer(@NotNull Player player, @NotNull Rank rank)
            throws FactionIsFrozenException {
        if (isFrozen()) throw new FactionIsFrozenException(registry);
        if (!Utility.callEvent(new FactionJoinEvent(this, player))) return false;

        factionMembers.join(player);
        factionPermissions.setRank(player, rank.getRegistryName());
        return true;
    }

    /**
     * Removes a player from the faction
     *
     * @param player The player to leave the faction.
     * @return If the player was able to leave
     */
    @Override
    public boolean leavePlayer(@NotNull Player player) throws FactionIsFrozenException {
        if (isFrozen()) throw new FactionIsFrozenException(registry);
        if (!Utility.callEvent(new FactionLeaveEvent(this, player))) return false;

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
    public boolean kickPlayer(@NotNull OfflinePlayer player) throws FactionIsFrozenException {
        if (isFrozen()) throw new FactionIsFrozenException(registry);
        if (!Utility.callEvent(new FactionKickEvent(this, player))) return false;

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
    public boolean banPlayer(@NotNull OfflinePlayer player)
            throws FactionIsFrozenException {
        if (isFrozen()) throw new FactionIsFrozenException(registry);
        if (!Utility.callEvent(new FactionBanEvent(this, player))) return false;

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
    public boolean pardonPlayer(@NotNull OfflinePlayer player)
            throws FactionIsFrozenException {
        if (isFrozen()) throw new FactionIsFrozenException(registry);
        if (!Utility.callEvent(new FactionUnbanEvent(this, player))) return false;

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
    public boolean isBanned(@NotNull OfflinePlayer player) {
        return factionMembers.getBanned().contains(player.getUniqueId());
    }

    /**
     * Returns the power of the faction
     * It sums all player power
     *
     * @return A BigDecimal representing the total power
     */
    @Override
    public @NotNull BigDecimal getPower() {
        return factionMembers.getMembers().stream().reduce(BigDecimal.ZERO,
                (bigDecimal, uuid) -> bigDecimal.add(BigDecimal.valueOf(playerPower(uuid))),
                BigDecimal::add);
    }

    /**
     * Returns the maximum power that can be generated by the faction
     * It sums all player's maxpower
     *
     * @return A BigDecimal representing the max reachable power
     */
    @Override
    public @NotNull BigDecimal getMaxPower() {
        return factionMembers.getMembers().stream().reduce(BigDecimal.ZERO,
                (bigDecimal, uuid) -> bigDecimal.add(BigDecimal.valueOf(maxPlayerPower(uuid))),
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
        throw new NotImplementedException("PowerPerPlayer isn't implemented for local faction yet. Please do it Tobero!");
    }

    /**
     * Returns the maximum power of the given player.
     *
     * @param player The player's UUID
     * @return The maximum power of the player.
     */
    @Override
    public double maxPlayerPower(@NotNull UUID player) {
        throw new NotImplementedException("PowerPerPlayer isn't implemented for local faction yet. Please do it Tobero!");
    }

    /**
     * Adds an ally to the faction
     *
     * @param faction The faction to add as an ally.
     * @return If the ally was able got added
     */
    @Override
    public boolean addAlly(@NotNull LocalFaction faction)
            throws FactionIsFrozenException {
        if (isFrozen()) throw new FactionIsFrozenException(registry);

        String registry = faction.registry;
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
    public boolean isAllied(@NotNull OfflinePlayer player) {
        Faction<?> faction = FactionManager.getPlayerFaction(player);
        return isAllied(faction.getRegistry());
    }

    /**
     * Adds an enemy to the faction.
     *
     * @param faction The faction to add as an enemy.
     * @return If the faction got added as enemy
     */
    @Override
    public boolean addEnemy(@NotNull LocalFaction faction)
            throws FactionIsFrozenException {
        if (isFrozen()) throw new FactionIsFrozenException(registry);

        String registry = faction.registry;
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
    public boolean resetRelation(@NotNull LocalFaction faction)
            throws FactionIsFrozenException {
        if (isFrozen()) throw new FactionIsFrozenException(registry);

        relationManager.getEnemies().remove(faction.registry);
        relationManager.getAllies().remove(faction.registry);

        return true;
    }

    /**
     * Returns the claims of this faction.
     *
     * @return A list of claims.
     */
    @Override
    public @NotNull FactionClaims getClaims() {
        return FactionClaims.createClaims(this);
    }

    /**
     * Get a setting by name
     *
     * @param setting The name of the setting you want to get.
     * @return A setting instance
     */
    @Override
    public @NotNull Setting<?> getSetting(@NotNull String setting) throws SettingNotFoundException {
        return factionPermissions.getFactionSettings().get(setting);
    }

    /**
     * Returns the module with the given name, or null if no such module exists
     *
     * @param moduleRegistry The name of the module you want to get.
     * @return A module that is registered to the module registry.
     */
    @Override
    public @Nullable FactionModule<LocalFaction> getModule(@NotNull String moduleRegistry) {
        return modules.get(moduleRegistry);
    }

    /**
     * It creates a new instance of the class you pass in, and adds it to the list of modules
     *
     * @param clazz      The class of the module you want to create.
     * @param parameters The values you want to feed into the constructor
     */
    @Override
    public <C extends FactionModule<LocalFaction>> void createModule(@NotNull Class<C> clazz, Object... parameters)
            throws NoSuchMethodException, InvocationTargetException,
            InstantiationException, IllegalAccessException {

        LinkedList<Class<?>> classes = new LinkedList<>(Arrays.stream(parameters)
                .filter(Objects::nonNull)
                .map(Object::getClass)
                .toList());

        clazz.getConstructor(classes.toArray(Class[]::new))
                .newInstance(parameters);
    }
}
