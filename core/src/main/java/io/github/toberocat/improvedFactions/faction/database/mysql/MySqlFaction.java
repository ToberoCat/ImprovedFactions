package io.github.toberocat.improvedFactions.faction.database.mysql;

import io.github.toberocat.improvedFactions.database.DatabaseVar;
import io.github.toberocat.improvedFactions.database.MySqlDatabase;
import io.github.toberocat.improvedFactions.database.builder.Select;
import io.github.toberocat.improvedFactions.event.EventExecutor;
import io.github.toberocat.improvedFactions.exceptions.description.DescriptionHasNoLine;
import io.github.toberocat.improvedFactions.exceptions.faction.FactionHandlerNotFound;
import io.github.toberocat.improvedFactions.exceptions.faction.FactionIsFrozenException;
import io.github.toberocat.improvedFactions.exceptions.faction.IllegalFactionNamingException;
import io.github.toberocat.improvedFactions.faction.Faction;
import io.github.toberocat.improvedFactions.faction.OpenType;
import io.github.toberocat.improvedFactions.faction.components.Description;
import io.github.toberocat.improvedFactions.faction.components.rank.GuestRank;
import io.github.toberocat.improvedFactions.faction.components.rank.Rank;
import io.github.toberocat.improvedFactions.faction.components.rank.members.FactionAdminRank;
import io.github.toberocat.improvedFactions.faction.components.rank.members.FactionOwnerRank;
import io.github.toberocat.improvedFactions.faction.components.rank.members.FactionRank;
import io.github.toberocat.improvedFactions.faction.database.mysql.module.MySqlModule;
import io.github.toberocat.improvedFactions.handler.ConfigHandler;
import io.github.toberocat.improvedFactions.handler.DatabaseHandler;
import io.github.toberocat.improvedFactions.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.player.FactionPlayer;
import io.github.toberocat.improvedFactions.player.OfflineFactionPlayer;
import io.github.toberocat.improvedFactions.utils.DateUtils;
import org.jetbrains.annotations.NotNull;
import org.joda.time.LocalDateTime;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.*;
import java.util.stream.Stream;

/**
 * This faction needs to sync up with the database none stop, so that there won't be any
 * problems when data is changed by other Plugin instances (BungeeCord, Velocity, Proxy stuff)
 * <p>
 * To allow the best performance, these two implementations are seperated by different classes,
 * so that there are no unnecessary if statements to check if it should sync now or not
 */
public class MySqlFaction implements Faction<MySqlFaction> {

    private final MySqlDatabase database;
    private final LinkedHashMap<String, MySqlModule> modules = new LinkedHashMap<>();

    private String registry;
    private String display;
    private String motd;
    private String tag;

    /**
     * Use FactionHandler#createFaction() to create a faction
     */
    public MySqlFaction() {
        this.database = DatabaseHandler.api().getMySql();
    }

    /**
     * @param display This name isn't allowed to be longer than "faction.maxNameLen" in config.yml
     */
    public MySqlFaction(@NotNull String display, @NotNull FactionPlayer<?> owner)
            throws IllegalFactionNamingException {
        this();
        this.registry = Faction.displayToRegistry(display);
        this.display = Faction.validateDisplay(display);

        if (!Faction.validNaming(registry)) throw new IllegalFactionNamingException(this, registry);

        ConfigHandler config = ConfigHandler.api();
        this.motd = config.getString("faction.default.motd", "Improved faction");
        this.tag = config.getString("faction.default.tag", "IFF");

        database.executeUpdate(MySqlDatabase.CREATE_LAYOUT_QUERY,
                DatabaseVar.of("registry", registry),
                DatabaseVar.of("display", display),
                DatabaseVar.of("motd", motd),
                DatabaseVar.of("tag", tag),
                DatabaseVar.of("openType", OpenType
                        .valueOf(config.getString("faction.default.open-type", "INVITE_ONLY")).ordinal()),
                DatabaseVar.of("frozen", config.getBool("faction.default.frozen")),
                DatabaseVar.of("permanent", config.getBool("faction.default.permanent")),
                DatabaseVar.of("created_at", DateUtils.TIME_FORMAT.print(new LocalDateTime())),
                DatabaseVar.of("owner", owner.getUniqueId().toString()),
                DatabaseVar.of("claimed_chunks", 0),
                DatabaseVar.of("balance", config.getInt("faction.default.start-balance")),
                DatabaseVar.of("current_power", config.getInt("faction.default.start-power")),
                DatabaseVar.of("max_power", config.getInt("faction.default.start-max-power"))
        );
    }

    /**
     * The registry is the only value that can't change, so no syncing is required
     *
     * @return The faction registry
     */
    @Override
    public @NotNull String getRegistry() {
        return registry;
    }

    /**
     * Get the display name of the faction from the database, or if it's not in the database,
     * return the display name that is stored locally.
     *
     * @return The display name of the faction.
     */
    @Override
    public @NotNull String getDisplay() {
        return database
                .rowSelect(new Select()
                        .setTable("factions")
                        .setColumns("display_name")
                        .setFilter("registry_id = %s", registry))
                .readRow(String.class, "display_name")
                .orElse(display);
    }

    /**
     * Set the display name of the faction to the given value locally, and update the database.
     *
     * @param display The display name of the faction.
     * @throws FactionIsFrozenException gets thrown when the faction is frozen
     */
    @Override
    public void setDisplay(@NotNull String display) throws FactionIsFrozenException {
        if (isFrozen()) throw new FactionIsFrozenException(registry);

        this.display = display;
        database.executeUpdate("UPDATE factions SET " +
                "display_name = %s WHERE registry_id = %s", display, registry);
    }

    /**
     * Get the faction color
     *
     * @return The color of the faction.
     */
    @Override
    public int getColor() {
        return FactionColors.values()[((EnumSetting) getSetting("color"))
                .getSelected()]
                .getColor();
    }

    /**
     * Get the motd from the database, or if it's not there, return
     * the motd that is stored locally.
     *
     * @return The motd of the faction.
     */
    @Override
    public @NotNull String getMotd() {
        return database
                .rowSelect(new Select()
                        .setTable("factions")
                        .setColumns("motd")
                        .setFilter("registry_id = %s", registry))
                .readRow(String.class, "motd")
                .orElse(motd);
    }

    /**
     * Set the motd of the faction and update the database.
     *
     * @param motd The new motd
     */
    @Override
    public void setMotd(@NotNull String motd) throws FactionIsFrozenException {
        if (isFrozen()) throw new FactionIsFrozenException(registry);

        this.motd = motd;
        database.executeUpdate("UPDATE factions SET motd = %s WHERE registry_id = %s", motd, registry);
    }

    /**
     * Get the tag of the faction, get it from the database,
     * if there is no tag, use the cached one
     *
     * @return The tag of the faction.
     */
    @Override
    public @NotNull String getTag() {
        return database
                .rowSelect(new Select()
                        .setTable("factions")
                        .setColumns("tag")
                        .setFilter("registry_id = %s", registry))
                .readRow(String.class, "tag")
                .orElse(tag);
    }

    /**
     * Set the tag of the faction to the given tag, and update the database.
     *
     * @param tag The new tag of the faction
     */
    @Override
    public void setTag(@NotNull String tag) throws FactionIsFrozenException {
        if (isFrozen()) throw new FactionIsFrozenException(registry);

        this.tag = tag;
        database.executeUpdate("UPDATE factions SET tag = %s WHERE registry_id = %s", tag, registry);
    }

    /**
     * Return a new Description object that uses the database to store the description.
     * <p>
     * The first thing we do is create a new Description object. This is an
     * anonymous class
     *
     * @return A new instance of the Description class.
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
                return database
                        .rowSelect(new Select()
                                .setTable("faction_descriptions")
                                .setColumns("content")
                                .setFilter("registry_id = %s", registry))
                        .getRows().stream().map(x -> x.get("content").toString());
            }

            /**
             * Get the line of the description with the given line number.
             *
             * The first thing done  is checking if the line number
             * is valid. If it isn't, an exception will be thrown
             *
             * @param line The line number to get.
             * @return The corresponding line
             */
            @Override
            public @NotNull String getLine(int line) throws DescriptionHasNoLine {
                return database
                        .rowSelect(new Select()
                                .setTable("faction_descriptions")
                                .setColumns("content")
                                .setFilter("registry_id = %s AND line = %d", registry, line))
                        .readRow(String.class, "content")
                        .orElse("");
            }

            @Override
            public void setLine(int line, @NotNull String content) throws FactionIsFrozenException {
                if (isFrozen()) throw new FactionIsFrozenException(registry);

                if (hasLine(line)) database
                        .executeUpdate("UPDATE faction_descriptions SET content = %s " +
                                        "WHERE registry_id = %s AND line = %s",
                                content, registry, line);
                else database
                        .executeUpdate("INSERT INTO faction_descriptions VALUE (%s, %d, %s)",
                                registry, line, content);
            }

            @Override
            public boolean hasLine(int line) {
                return database
                        .rowSelect(new Select()
                                .setTable("faction_descriptions")
                                .setColumns("content")
                                .setFilter("registry_id = %s AND line = %d", registry, line))
                        .readRow(String.class, "content")
                        .isPresent();
            }

            @Override
            public int getLastLine() {
                return database
                        .rowSelect(new Select()
                                .setTable("faction_descriptions")
                                .setColumns("line")
                                .setFilter("registry_id = %s ORDER BY line DESC", registry))
                        .readRow(Integer.class, "line")
                        .orElse(0);
            }
        };
    }

    @Override
    public @NotNull String getCreatedAt() {
        return database
                .rowSelect(new Select()
                        .setTable("factions")
                        .setColumns("created_at")
                        .setFilter("registry_id = %s", registry))
                .readRow(String.class, "created_at")
                .orElse(DateUtils.TIME_FORMAT.print(new LocalDateTime()));
    }

    @Override
    public @NotNull OpenType getType() {
        return OpenType
                .values()[database
                .rowSelect(new Select()
                        .setTable("faction_descriptions")
                        .setColumns("open_type")
                        .setFilter("registry_id = %s", registry))
                .readRow(Integer.class, "open_type")
                .orElse(1)];
    }

    @Override
    public void setType(@NotNull OpenType type) throws FactionIsFrozenException {
        if (isFrozen()) throw new FactionIsFrozenException(registry);

        database
                .executeUpdate("UPDATE factions SET open_type = %d WHERE registry_id = %s",
                        type.ordinal(), registry);
    }

    @Override
    public @NotNull UUID getOwner() {
        return UUID.fromString(database.rowSelect(new Select()
                        .setTable("factions")
                        .setColumns("owner")
                        .setFilter("registry_id = %s", registry))
                .readRow(String.class, "owner")
                .orElseThrow());
    }

    @Override
    public boolean isPermanent() {
        return database.rowSelect(new Select()
                        .setTable("factions")
                        .setColumns("permanent")
                        .setFilter("WHERE registry_id = %s", registry))
                .readRow(Boolean.class, "permanent")
                .orElse(false);
    }

    @Override
    public void setPermanent(boolean permanent) {
        database
                .executeUpdate("UPDATE factions SET permanent = %b WHERE registry_id = %s",
                        permanent, registry);
    }

    @Override
    public boolean isFrozen() {
        return database.rowSelect(new Select()
                        .setTable("factions")
                        .setColumns("frozen")
                        .setFilter("WHERE registry_id = %s", registry))
                .readRow(Boolean.class, "frozen")
                .orElse(false);
    }

    @Override
    public void setFrozen(boolean frozen) {
        database
                .executeUpdate("UPDATE factions SET frozen = %b WHERE registry_id = %s",
                        frozen, registry);
    }

    @Override
    public @NotNull Rank getPlayerRank(@NotNull OfflineFactionPlayer<?> player) {
        if (isMember(player)) return getDbRank(player);
        if (isAllied(player)) return getDbRank(player).getEquivalent();
        return Rank.fromString(GuestRank.register);
    }

    @Override
    public @NotNull RankSetting getPermission(@NotNull String permission)
            throws SettingNotFoundException {
        Map.Entry<String, String> entry = getSettingValueType(permission);
        String type = entry.getKey();
        String value = entry.getValue();

        try {
            Class<?> clazz = Class.forName(type);
            if (!clazz.isAssignableFrom(RankSetting.class))
                throw new SettingNotFoundException(permission);

            RankSetting setting = (RankSetting) clazz.getConstructor().newInstance();
            setting.fromString(value);
            return setting;
        } catch (ClassNotFoundException | NoSuchMethodException |
                InstantiationException | IllegalAccessException |
                InvocationTargetException ignored) {
            throw new SettingNotFoundException(permission);
        }
    }

    private @NotNull FactionRank getDbRank(@NotNull OfflineFactionPlayer<?> player) {
        MySqlFactionHandler handler = MySqlFactionHandler.getInstance();

        if (handler == null) throw new FactionHandlerNotFound("A database faction " +
                "required a database handler, but didn't find it. " +
                "This is a critical bug and needs to be reported to the dev using discord / github");
        return handler.getSavedRank(player);
    }

    @Override
    public boolean hasPermission(@NotNull OfflineFactionPlayer<?> player,
                                 @NotNull String permission) throws SettingNotFoundException {
        RankSetting setting = getPermission(permission);
        return setting.hasPermission(getPlayerRank(player));
    }

    @Override
    public boolean isMember(@NotNull OfflineFactionPlayer<?> player) {
        return database.rowSelect(new Select()
                        .setTable("players")
                        .setColumns("faction")
                        .setFilter("uuid = %s", player.getUniqueId().toString()))
                .readRow(String.class, "faction")
                .orElse("")
                .equals(registry);
    }

    @Override
    public boolean isAllied(@NotNull OfflineFactionPlayer<?> player) {
        return getAllies().anyMatch(x -> x.equals(database.rowSelect(new Select()
                        .setTable("players")
                        .setColumns("faction")
                        .setFilter("uuid = %s", player.getUniqueId().toString()))
                .readRow(String.class, "faction")
                .orElse("")));
    }

    /**
     * @param player The player you want to change the rank of.
     * @param rank   The rank you want to change the player to.
     */
    @Override
    public void changeRank(@NotNull OfflineFactionPlayer<?> player, @NotNull FactionRank rank)
            throws FactionIsFrozenException {
        if (isFrozen()) throw new FactionIsFrozenException(registry);

        if (!isMember(player)) return;
        FactionRank previous = getDbRank(player);

        database.executeUpdate("UPDATE players SET member_rank = %s WHERE uuid = %s",
                rank.getRegistry(), player.getUniqueId().toString());
        EventExecutor.getExecutor().factionMemberRankUpdate(this,
                player,
                previous,
                rank);
    }

    @Override
    public void transferOwnership(@NotNull FactionPlayer<?> player) throws FactionIsFrozenException {
        if (isFrozen()) throw new FactionIsFrozenException(registry);

        OfflineFactionPlayer<?> old = ImprovedFactions.api().getOfflinePlayer(getOwner());
        if (old == null) return;

        changeRank(old, (FactionRank) Rank.fromString(FactionAdminRank.REGISTRY));

        changeRank(player, (FactionRank) Rank.fromString(FactionOwnerRank.REGISTRY));

        database.executeUpdate("UPDATE factions SET owner = %s WHERE registry_id = %s",
                        player.getUniqueId(),
                        registry);

        EventExecutor.getExecutor().transferOwnership(this, old, player);
    }

    @Override
    public void deleteFaction() throws FactionIsFrozenException {
        if (isFrozen()) throw new FactionIsFrozenException(registry);
        database.executeUpdate(MySqlDatabase.DELETE_FACTION, DatabaseVar.of("registry", registry));
    }

    @Override
    public @NotNull Stream<UUID> getBanned() {
        return database.rowSelect(new Select()
                        .setTable("faction_bans")
                        .setColumns("banned")
                        .setFilter("registry_id = %s", registry))
                .getRows()
                .stream()
                .map(x -> UUID.fromString(x.get("banned").toString()));
    }

    @Override
    public @NotNull Stream<UUID> getMembers() {
        return database.rowSelect(new Select()
                        .setTable("players")
                        .setColumns("uuid")
                        .setFilter("faction = %s", registry))
                .getRows()
                .stream()
                .map(x -> UUID.fromString(x.get("uuid").toString()));
    }

    @Override
    public boolean joinPlayer(@NotNull FactionPlayer<?> player) throws FactionIsFrozenException {
        return joinPlayer(player, (FactionRank) Rank.fromString(FactionOwnerRank.REGISTRY));
    }

    @Override
    public boolean joinPlayer(@NotNull FactionPlayer<?> player, @NotNull FactionRank rank)
            throws FactionIsFrozenException {
        if (isFrozen()) throw new FactionIsFrozenException(registry);
        if (isMember(player)) return false;

        database.executeUpdate("UPDATE players SET faction = %s WHERE uuid = %s",
                registry, player.getUniqueId().toString());
        database.executeUpdate("UPDATE players SET member_rank = %s WHERE uuid = %s",
                        rank.getRegistry(), player.getUniqueId().toString());

        EventExecutor.getExecutor().joinMember(this, player, rank);
        return true;
    }

    @Override
    public boolean leavePlayer(@NotNull FactionPlayer<?> player)
            throws FactionIsFrozenException, PlayerIsOwnerException {
        if (isFrozen()) throw new FactionIsFrozenException(registry);
        if (!isPermanent() && getPlayerRank(player).getRegistryName().equals(OwnerRank.registry))
            throw new PlayerIsOwnerException(this, player);

        if (setGuestRank(player)) return false;
        AsyncTask.callEventSync(new FactionLeaveEvent(this, player));
        return true;
    }

    @Override
    public boolean kickPlayer(@NotNull OfflinePlayer player) throws FactionIsFrozenException {
        if (isFrozen()) throw new FactionIsFrozenException(registry);
        if (setGuestRank(player)) return false;
        AsyncTask.callEventSync(new FactionKickEvent(this, player));
        return true;
    }


    @Override
    public boolean banPlayer(@NotNull OfflinePlayer player) throws FactionIsFrozenException {
        if (isFrozen()) throw new FactionIsFrozenException(registry);
        if (setGuestRank(player)) return false;
        database.evalTry("INSERT INTO faction_bans VALUE (%s, %s)", registry,
                        player.getUniqueId().toString())
                .get(PreparedStatement::executeUpdate);
        AsyncTask.callEventSync(new FactionBanEvent(this, player));
        return true;
    }

    private boolean setGuestRank(@NotNull OfflinePlayer player) {
        if (!isMember(player)) return true;

        database.evalTry("UPDATE players SET faction = %s WHERE uuid = %s",
                        null, player.getUniqueId().toString())
                .get(PreparedStatement::executeUpdate);

        Rank previous = getDbRank(player);

        String guest = GuestRank.register;
        database.evalTry("UPDATE players SET member_rank = %s WHERE uuid = %s",
                        guest, player.getUniqueId().toString())
                .get(PreparedStatement::executeUpdate);
        AsyncTask.callEventSync(new FactionUpdateMemberRankEvent(
                this,
                player,
                previous.getRegistryName(),
                guest));
        return false;
    }

    @Override
    public boolean pardonPlayer(@NotNull OfflinePlayer player) throws FactionIsFrozenException {
        if (isFrozen()) throw new FactionIsFrozenException(registry);
        if (!isBanned(player)) return false;

        database.evalTry("DELETE FROM faction_bans WHERE banned = %s AND regisry_id = %s",
                        player.getUniqueId().toString(),
                        registry)
                .get(PreparedStatement::executeUpdate);
        AsyncTask.callEventSync(new FactionUnbanEvent(this, player));
        return true;
    }

    @Override
    public boolean isBanned(@NotNull OfflinePlayer player) {
        return database.rowSelect(new Select()
                        .setTable(Table.FACTION_BANS.getTable())
                        .setColumns("banned")
                        .setFilter("registry_id = %s AND banned = %s",
                                registry,
                                player.getUniqueId().toString()))
                .getRows().size() != 0;
    }

    @Override
    public @NotNull BigDecimal getTotalPower() {
        return database.rowSelect(new Select()
                        .setTable(Table.PLAYERS.getTable())
                        .setColumns("power")
                        .setFilter("faction = %s", registry))
                .getRows()
                .stream()
                .reduce(BigDecimal.ZERO,
                        (bigDecimal, row) ->
                                bigDecimal.add(BigDecimal.valueOf((double) row.get("power"))),
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

    @Override
    public @NotNull BigDecimal getTotalMaxPower() {
        return database.rowSelect(new Select()
                        .setTable(Table.PLAYERS.getTable())
                        .setColumns("uuid", "power")
                        .setFilter("faction = %s", registry))
                .getRows()
                .stream()
                .reduce(BigDecimal.ZERO,
                        (bigDecimal, row) ->
                                bigDecimal.add(BigDecimal.valueOf((double) row.get("maxPower"))),
                        BigDecimal::add);
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
                        (bigDecimal, player) ->
                                bigDecimal.add(BigDecimal.valueOf(
                                        maxPlayerPower(player.getUniqueId()))),
                        BigDecimal::add);
    }

    @Override
    public double playerPower(@NotNull UUID player) {
        return database.rowSelect(new Select()
                        .setTable(Table.PLAYERS.getTable())
                        .setColumns("power")
                        .setFilter("uuid = %s", player.toString()))
                .readRow(Double.class, "power")
                .orElse(Double.NaN);
    }

    @Override
    public double maxPlayerPower(@NotNull UUID player) {
        return database.rowSelect(new Select()
                        .setTable(Table.PLAYERS.getTable())
                        .setColumns("maxPower")
                        .setFilter("uuid = %s", player.toString()))
                .readRow(Double.class, "maxPower")
                .orElse(Double.NaN);
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
        if (isFrozen()) throw new FactionIsFrozenException(registry);
        if (faction.getRegistry().equals(registry)) throw new CantInviteYourselfException();
        if (hasInvited(faction)) throw new AlreadyInvitedException(faction);

        Player invitedOwner = Bukkit.getPlayer(faction.getOwner());
        if (invitedOwner == null) throw new FactionOwnerIsOfflineException(faction);

        database.evalTry("INSERT INTO ally_invites VALUES (%s, %s, NOW())",
                        registry, faction.getRegistry())
                .get(PreparedStatement::executeUpdate);
    }

    /**
     * Removes an invitation to be allied with your faction
     *
     * @param faction The faction to remove the invite from.
     */
    @Override
    public void removeAllyInvite(@NotNull Faction<?> faction) {
        database.evalTry("DELETE FROM ally_invites WHERE sender = %s AND receiver = %s",
                        registry, faction.getRegistry())
                .get(PreparedStatement::executeUpdate);
    }

    /**
     * Returns true if the faction got already invited
     *
     * @param faction@return if invited
     */
    @Override
    public boolean hasInvited(@NotNull Faction<?> faction) {
        return database.rowSelect(new Select()
                        .setTable("ally_invites")
                        .setColumns("")
                        .setFilter("sender = %s AND receiver = %s",
                                registry, faction.getRegistry()))
                .getRows().size() != 0;
    }

    /**
     * Returns true if this faction was invited by the given faction.
     *
     * @param faction The faction you want to check if you got invited by.
     * @return If invited
     */
    @Override
    public boolean hasBeenInvitedBy(@NotNull Faction<?> faction) {
        return database.rowSelect(new Select()
                        .setTable("ally_invites")
                        .setColumns("")
                        .setFilter("sender = %s AND receiver = %s",
                                faction.getRegistry(), registry))
                .getRows().size() != 0;
    }

    /**
     * Returns a stream of all the UUIDs of the invites that have been sent by your faction
     *
     * @return A stream of faction registries
     */
    @Override
    public @NotNull Stream<String> getSentInvites() {
        return database.rowSelect(new Select()
                        .setTable("ally_invites")
                        .setColumns("receiver")
                        .setFilter("sender = %s", registry))
                .getRows()
                .stream().map(x -> x.get("receiver").toString());
    }

    /**
     * Returns a stream of all the invites that the faction has received.
     *
     * @return A stream of faction registries
     */
    @Override
    public @NotNull Stream<String> getReceivedInvites() {
        return database.rowSelect(new Select()
                        .setTable("ally_invites")
                        .setColumns("sender")
                        .setFilter("receiver = %s", registry))
                .getRows()
                .stream().map(x -> x.get("sender").toString());
    }

    @Override
    public boolean addAlly(@NotNull Faction<?> faction) throws FactionIsFrozenException {
        if (setStatus(faction, allyId)) return false;

        AsyncTask.callEventSync(new FactionAllyEvent(this, faction));
        return true;
    }

    @Override
    public boolean isAllied(@NotNull String registry) {
        return database.rowSelect(new Select()
                        .setTable(Table.FACTION_RELATIONS.getTable())
                        .setColumns("relation_status")
                        .setFilter("registry_id = %s", registry))
                .readRow(Integer.class, "relation_status")
                .orElse(neutralId)
                == allyId;
    }

    @Override
    public boolean addEnemy(@NotNull Faction<?> faction) throws FactionIsFrozenException {
        return !setStatus(faction, enemyId);
    }

    private boolean setStatus(@NotNull Faction<?> faction, int status)
            throws FactionIsFrozenException {
        if (isFrozen()) throw new FactionIsFrozenException(registry);

        String registry = faction.getRegistry();
        if (isAllied(registry) || isEnemy(registry)) return true;

        database.evalTry("INSERT INTO faction_relations VALUES (%s, %s, %d), (%s, %s, %d)",
                        this.registry, registry, status,
                        registry, this.registry, status)
                .get(PreparedStatement::executeUpdate);
        return false;
    }

    @Override
    public boolean isEnemy(@NotNull String registry) {
        return database.rowSelect(new Select()
                        .setTable(Table.FACTION_RELATIONS.getTable())
                        .setColumns("relation_status")
                        .setFilter("registry_id = %s", registry))
                .readRow(Integer.class, "relation_status")
                .orElse(neutralId)
                == enemyId;
    }

    /**
     * Returns true if the given player is an enemy of this faction
     *
     * @param player The player to check.
     * @return If the player's faction is an enemy
     */
    @Override
    public boolean isEnemy(@NotNull OfflinePlayer player) {
        return false;
    }

    @Override
    public @NotNull Stream<String> getAllies() {
        return database.rowSelect(new Select()
                        .setTable(Table.FACTION_RELATIONS.getTable())
                        .setColumns("relation_registry_id")
                        .setFilter("registry_id = %s AND relation_status = %d",
                                registry, allyId))
                .getRows()
                .stream()
                .map(x -> x.get("relation_registry_id").toString());
    }

    @Override
    public @NotNull Stream<String> getEnemies() {
        return database.rowSelect(new Select()
                        .setTable(Table.FACTION_RELATIONS.getTable())
                        .setColumns("relation_registry_id")
                        .setFilter("registry_id = %s AND relation_status = %d",
                                registry, enemyId))
                .getRows()
                .stream()
                .map(x -> x.get("relation_registry_id").toString());
    }

    @Override
    public boolean resetRelation(@NotNull Faction<?> faction)
            throws FactionIsFrozenException {
        if (isFrozen()) throw new FactionIsFrozenException(registry);

        String registry = faction.getRegistry();

        database.evalTry("DELETE FROM faction_relations WHERE " +
                        "relation_registry_id = %s", registry)
                .get(PreparedStatement::executeUpdate);

        return true;
    }

    /**
     * BroadcastMessage takes a String as message that should egt sent to everyone
     *
     * @param msg The message to broadcast to all members.
     */
    @Override
    public void broadcastMessage(@NotNull String msg) {
        getMembers()
                .map(Bukkit::getPlayer)
                .filter(Objects::nonNull)
                .forEach(player -> player.sendMessage(msg));
    }

    /**
     * Broadcast a translatable message to all players.
     * The translation will be individual for each player based on their selected language
     *
     * @param key The key of the translatable message.
     */
    @Override
    public void broadcastTranslatable(@NotNull String key, Parseable... parseables) {
        getMembers()
                .map(Bukkit::getPlayer)
                .filter(Objects::nonNull)
                .forEach(player -> Language.sendMessage(key, player, parseables));
    }

    @Override
    public @NotNull FactionClaims<MySqlFaction> getClaims() {
        return FactionClaims.createClaims(this);
    }

    @Override
    public @NotNull Setting<?> getSetting(@NotNull String setting)
            throws SettingNotFoundException {
        Map.Entry<String, String> entry = getSettingValueType(setting);
        String type = entry.getKey();
        String value = entry.getValue();
        try {
            Class<?> clazz = Class.forName(type);
            if (!clazz.isAssignableFrom(Setting.class))
                throw new SettingNotFoundException(setting);

            Setting<?> s = (Setting<?>) clazz.getConstructor().newInstance();
            s.fromString(value);
            return s;
        } catch (ClassNotFoundException | NoSuchMethodException |
                InstantiationException | IllegalAccessException |
                InvocationTargetException ignored) {
            throw new SettingNotFoundException(setting);
        }
    }

    @Override
    public @NotNull FactionReports getReports() {
        return new FactionReports() {
            @Override
            public void addReport(@NotNull Player reporter, @NotNull String reason) {
                database.tableInsert(new Insert()
                        .setTable("reports")
                        .setColumns("registry", "reporter", "reason")
                        .setData(registry, reporter.getUniqueId().toString(), reason));
            }

            @Override
            public @NotNull Stream<Report> getReports() {
                return database.rowSelect(new Select()
                                .setTable("reports")
                                .setColumns("reporter", "reasons")
                                .setFilter("registry = %s", registry))
                        .getRows()
                        .stream().map(x -> new Report(UUID
                                .fromString(x.get("reporter").toString()),
                                x.get("reason").toString()));
            }
        };
    }

    private @NotNull Map.Entry<String, String> getSettingValueType(@NotNull String setting)
            throws SettingNotFoundException {
        List<Row> rows = database.rowSelect(new Select()
                        .setTable(Table.FACTION_SETTINGS.getTable())
                        .setColumns("type", "value")
                        .setFilter("registry_id = %s, setting = %s", registry, setting))
                .getRows();
        if (rows == null || rows.size() == 0) throw new SettingNotFoundException(setting);

        Row row = rows.get(0);
        if (row == null) throw new SettingNotFoundException(setting);

        String type = row.get("type").toString();
        String value = row.get("value").toString();

        return Map.entry(type, value);
    }

    @Override
    public @Nullable DatabaseModule getModule(@NotNull String registry) {
        return modules.get(registry);
    }

    @Override
    public <C extends FactionModule<MySqlFaction>>
    void createModule(@NotNull Class<C> clazz, Object... parameters)
            throws NoSuchMethodException, InvocationTargetException,
            InstantiationException, IllegalAccessException {

        LinkedList<Class<?>> classes = new LinkedList<>(Arrays.stream(parameters)
                .filter(Objects::nonNull)
                .map(Object::getClass)
                .toList());

        FactionModule<MySqlFaction> module = clazz.getConstructor(classes.toArray(Class[]::new))
                .newInstance(parameters);
        modules.put(module.registry(), (DatabaseModule) module);
    }

    @Override
    public String toString() {
        return getRegistry();
    }
}
