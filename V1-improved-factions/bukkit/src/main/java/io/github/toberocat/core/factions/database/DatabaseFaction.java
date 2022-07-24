package io.github.toberocat.core.factions.database;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.OpenType;
import io.github.toberocat.core.factions.components.Description;
import io.github.toberocat.core.factions.components.FactionClaims;
import io.github.toberocat.core.factions.components.FactionModule;
import io.github.toberocat.core.factions.components.rank.GuestRank;
import io.github.toberocat.core.factions.components.rank.Rank;
import io.github.toberocat.core.factions.components.rank.members.AdminRank;
import io.github.toberocat.core.factions.components.rank.members.FactionRank;
import io.github.toberocat.core.factions.components.rank.members.OwnerRank;
import io.github.toberocat.core.factions.database.module.DatabaseModule;
import io.github.toberocat.core.utility.async.AsyncTask;
import io.github.toberocat.core.utility.color.FactionColors;
import io.github.toberocat.core.utility.data.Table;
import io.github.toberocat.core.utility.data.database.DatabaseAccess;
import io.github.toberocat.core.utility.data.database.sql.MySqlDatabase;
import io.github.toberocat.core.utility.data.database.sql.SqlCode;
import io.github.toberocat.core.utility.data.database.sql.SqlVar;
import io.github.toberocat.core.utility.data.database.sql.builder.Row;
import io.github.toberocat.core.utility.data.database.sql.builder.Select;
import io.github.toberocat.core.utility.date.DateCore;
import io.github.toberocat.core.utility.events.faction.*;
import io.github.toberocat.core.utility.exceptions.DescriptionHasNoLine;
import io.github.toberocat.core.utility.exceptions.faction.FactionHandlerNotFound;
import io.github.toberocat.core.utility.exceptions.faction.FactionIsFrozenException;
import io.github.toberocat.core.utility.exceptions.setting.SettingNotFoundException;
import io.github.toberocat.core.utility.settings.type.EnumSetting;
import io.github.toberocat.core.utility.settings.type.RankSetting;
import io.github.toberocat.core.utility.settings.type.Setting;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
public class DatabaseFaction extends Faction<DatabaseFaction> {

    private final MySqlDatabase database;
    private final LinkedHashMap<String, DatabaseModule> modules = new LinkedHashMap<>();

    private String registry;
    private String display;
    private String motd;
    private String tag;

    /**
     * Use FactionHandler#createFaction() to create a faction
     */
    public DatabaseFaction() {
        this.database = DatabaseAccess.accessPipeline(DatabaseAccess.class).database();
    }

    /**
     * @param display This name isn't allowed to be longer than "faction.maxNameLen" in config.yml
     */
    public DatabaseFaction(@NotNull String display, @NotNull Player owner) {
        this();
        this.registry = Faction.displayToRegistry(display);
        this.display = display.substring(0, MainIF.config().getInt("faction.maxNameLen"));

        FileConfiguration config = MainIF.config();
        this.motd = config.getString("faction.default.motd", "Improved faction");
        this.tag = config.getString("faction.default.tag", "IFF");

        SqlCode.execute(database, SqlCode.CREATE_FACTION,
                SqlVar.of("registry", registry),
                SqlVar.of("display", display),
                SqlVar.of("motd", motd),
                SqlVar.of("tag", tag),
                SqlVar.of("openType", OpenType.valueOf(config
                        .getString("faction.default.openType", "INVITE_ONLY")).ordinal()),
                SqlVar.of("frozen", config.getBoolean("faction.default.frozen")),
                SqlVar.of("permanent", config.getBoolean("faction.default.permanent")),
                SqlVar.of("created_at", DateCore.TIME_FORMAT.print(new LocalDateTime())),
                SqlVar.of("owner", owner.getUniqueId().toString()),
                SqlVar.of("claimed_chunks", 0),
                SqlVar.of("balance", 0),
                SqlVar.of("current_power", config.getInt("faction.default.power.value")),
                SqlVar.of("max_power", config.getInt("faction.default.power.max"))
        );
    }

    /**
     * It loads the faction from the database
     *
     * @param loadRegistry The registry ID of the faction to load.
     */
    @Override
    public void createFromStorage(@NotNull String loadRegistry) {
        HashMap<String, Object> map = database.rowSelect(new Select()
                        .setTable(Table.FACTIONS.getTable())
                        .setColumns("registry", "display", "motd", "tag")
                        .setFilter("registry_id = %s", loadRegistry))
                .getRows()
                .get(0)
                .getColumns();

        registry = map.get("registry").toString();
        display = map.get("display").toString();
        motd = map.get("motd").toString();
        tag = map.get("tag").toString();
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
                        .setTable(Table.FACTIONS.getTable())
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
        database
                .evalTry("UPDATE factions SET display_name = %s WHERE registry_id = %s",
                        display, registry)
                .get(PreparedStatement::executeUpdate);
    }

    /**
     * Get the faction color
     *
     * @return The color of the faction.
     */
    @Override
    public int getColor() throws SettingNotFoundException {
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
                        .setTable(Table.FACTIONS.getTable())
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
        database
                .evalTry("UPDATE factions SET motd = %s WHERE registry_id = %s",
                        motd, registry)
                .get(PreparedStatement::executeUpdate);
    }

    /**
     * Get the tag of the faction, get it from the database, if there is no tag, use the cached one
     *
     * @return The tag of the faction.
     */
    @Override
    public @NotNull String getTag() {
        return database
                .rowSelect(new Select()
                        .setTable(Table.FACTIONS.getTable())
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
        database
                .evalTry("UPDATE factions SET tag = %s WHERE registry_id = %s",
                        tag, registry)
                .get(PreparedStatement::executeUpdate);
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
                                .setTable(Table.FACTION_DESCRIPTIONS.getTable())
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
                                .setTable(Table.FACTION_DESCRIPTIONS.getTable())
                                .setColumns("content")
                                .setFilter("registry_id = %s AND line = %d", registry, line))
                        .readRow(String.class, "content")
                        .orElse("");
            }

            @Override
            public void setLine(int line, @NotNull String content) throws FactionIsFrozenException {
                if (isFrozen()) throw new FactionIsFrozenException(registry);

                if (hasLine(line)) database
                        .evalTry("UPDATE faction_descriptions SET content = %s " +
                                        "WHERE registry_id = %s AND line = %s",
                                content, registry, line)
                        .get(PreparedStatement::executeUpdate);
                else database
                        .evalTry("INSERT INTO faction_descriptions VALUE (%s, %d, %s)",
                                registry, line, content)
                        .get(PreparedStatement::executeUpdate);
            }

            @Override
            public boolean hasLine(int line) {
                return database
                        .rowSelect(new Select()
                                .setTable(Table.FACTION_DESCRIPTIONS.getTable())
                                .setColumns("content")
                                .setFilter("registry_id = %s AND line = %d", registry, line))
                        .readRow(String.class, "content")
                        .isPresent();
            }

            @Override
            public int getLastLine() {
                return database
                        .rowSelect(new Select()
                                .setTable(Table.FACTION_DESCRIPTIONS.getTable())
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
                        .setTable(Table.FACTIONS.getTable())
                        .setColumns("created_at")
                        .setFilter("registry_id = %s", registry))
                .readRow(String.class, "created_at")
                .orElse(DateCore.TIME_FORMAT.print(new LocalDateTime()));
    }

    @Override
    public @NotNull OpenType getType() {
        return OpenType
                .values()[database
                .rowSelect(new Select()
                        .setTable(Table.FACTIONS.getTable())
                        .setColumns("open_type")
                        .setFilter("registry_id = %s", registry))
                .readRow(Integer.class, "open_type")
                .orElse(1)];
    }

    @Override
    public void setType(@NotNull OpenType type) throws FactionIsFrozenException {
        if (isFrozen()) throw new FactionIsFrozenException(registry);

        database
                .evalTry("UPDATE factions SET open_type = %d WHERE registry_id = %s",
                        type.ordinal(), registry)
                .get(PreparedStatement::executeUpdate);
    }

    @Override
    public @NotNull UUID getOwner() {
        return UUID.fromString(database.rowSelect(new Select()
                        .setTable(Table.FACTIONS.getTable())
                        .setColumns("owner")
                        .setFilter("registry_id = %s", registry))
                .readRow(String.class, "owner")
                .orElseThrow());
    }

    @Override
    public boolean isPermanent() {
        return database.rowSelect(new Select()
                        .setTable(Table.FACTIONS.getTable())
                        .setColumns("permanent")
                        .setFilter("WHERE registry_id = %s", registry))
                .readRow(Boolean.class, "permanent")
                .orElse(false);
    }

    @Override
    public void setPermanent(boolean permanent) {
        database
                .evalTry("UPDATE factions SET permanent = %b WHERE registry_id = %s",
                        permanent, registry)
                .get(PreparedStatement::executeUpdate);
    }

    @Override
    public boolean isFrozen() {
        return database.rowSelect(new Select()
                        .setTable(Table.FACTIONS.getTable())
                        .setColumns("frozen")
                        .setFilter("WHERE registry_id = %s", registry))
                .readRow(Boolean.class, "frozen")
                .orElse(false);
    }

    @Override
    public void setFrozen(boolean frozen) {
        database
                .evalTry("UPDATE factions SET frozen = %b WHERE registry_id = %s",
                        frozen, registry)
                .get(PreparedStatement::executeUpdate);
    }

    @Override
    public @NotNull Rank getPlayerRank(@NotNull OfflinePlayer player) {
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
                InstantiationException | IllegalAccessException | InvocationTargetException ignored) {
            throw new SettingNotFoundException(permission);
        }
    }

    private @NotNull Rank getDbRank(@NotNull OfflinePlayer player) {
        DatabaseFactionHandler handler = DatabaseFactionHandler.getInstance();

        if (handler == null) throw new FactionHandlerNotFound("A database faction " +
                "required a database handler, but didn't find it. " +
                "This is a critical bug and needs to be reported to the dev using discord / github");
        return handler.getSavedRank(player);
    }

    @Override
    public boolean hasPermission(@NotNull OfflinePlayer player,
                                 @NotNull String permission) throws SettingNotFoundException {
        RankSetting setting = getPermission(permission);
        return setting.hasPermission(getPlayerRank(player));
    }

    @Override
    public boolean isMember(@NotNull OfflinePlayer player) {
        return database.rowSelect(new Select()
                        .setTable(Table.PLAYERS.getTable())
                        .setColumns("faction")
                        .setFilter("uuid = %s", player.getUniqueId().toString()))
                .readRow(String.class, "faction")
                .orElse("")
                .equals(registry);
    }

    @Override
    public boolean isAllied(@NotNull OfflinePlayer player) {
        return getAllies().anyMatch(x -> x.equals(database.rowSelect(new Select()
                        .setTable(Table.PLAYERS.getTable())
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
    public void changeRank(@NotNull OfflinePlayer player, @NotNull FactionRank rank)
            throws FactionIsFrozenException {
        if (isFrozen()) throw new FactionIsFrozenException(registry);

        if (!isMember(player)) return;
        Rank previous = getDbRank(player);

        database.evalTry("UPDATE players SET member_rank = %s WHERE uuid = %s",
                        rank.getRegistryName(), player.getUniqueId().toString())
                .get(PreparedStatement::executeUpdate);
        AsyncTask.callEventSync(new FactionUpdateMemberRankEvent(
                this,
                player,
                previous.getRegistryName(),
                rank.getRegistryName()));
    }

    @Override
    public void transferOwnership(@NotNull Player player) throws FactionIsFrozenException {
        if (isFrozen()) throw new FactionIsFrozenException(registry);

        OfflinePlayer old = Bukkit.getOfflinePlayer(getOwner());
        changeRank(old, (FactionRank) Rank.fromString(AdminRank.registry));

        changeRank(player,
                (FactionRank) Rank.fromString(OwnerRank.registry));

        database.evalTry("UPDATE factions SET owner = %s WHERE registry_id = %s",
                        player.getUniqueId(),
                        registry)
                .get(PreparedStatement::executeUpdate);

        AsyncTask.callEventSync(new FactionTransferOwnershipEvent(this, old, player));
    }

    @Override
    public void deleteFaction() throws FactionIsFrozenException {
        if (isFrozen()) throw new FactionIsFrozenException(registry);
        SqlCode.execute(database, SqlCode.DELETE_FACTION, SqlVar.of("registry", registry))
                .get(PreparedStatement::executeUpdate);
    }

    @Override
    public @NotNull Stream<UUID> getBanned() {
        return database.rowSelect(new Select()
                        .setTable(Table.FACTION_BANS.getTable())
                        .setColumns("banned")
                        .setFilter("registry_id = %s", registry))
                .getRows()
                .stream()
                .map(x -> UUID.fromString(x.get("banned").toString()));
    }

    @Override
    public @NotNull Stream<UUID> getMembers() {
        return database.rowSelect(new Select()
                        .setTable(Table.PLAYERS.getTable())
                        .setColumns("uuid")
                        .setFilter("faction = %s", registry))
                .getRows()
                .stream()
                .map(x -> UUID.fromString(x.get("uuid").toString()));
    }

    @Override
    public boolean joinPlayer(@NotNull Player player) throws FactionIsFrozenException {
        return joinPlayer(player, Rank.fromString(OwnerRank.registry));
    }

    @Override
    public boolean joinPlayer(@NotNull Player player, @NotNull Rank rank) throws FactionIsFrozenException {
        if (isFrozen()) throw new FactionIsFrozenException(registry);
        if (isMember(player)) return false;

        database.evalTry("UPDATE players SET faction = %s WHERE uuid = %s",
                        registry, player.getUniqueId().toString())
                .get(PreparedStatement::executeUpdate);
        database.evalTry("UPDATE players SET member_rank = %s WHERE uuid = %s",
                        rank.getRegistryName(), player.getUniqueId().toString())
                .get(PreparedStatement::executeUpdate);

        AsyncTask.callEventSync(new FactionJoinEvent(this, player));
        return true;
    }

    @Override
    public boolean leavePlayer(@NotNull Player player) throws FactionIsFrozenException {
        if (isFrozen()) throw new FactionIsFrozenException(registry);
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
                                bigDecimal.add(BigDecimal.valueOf(playerPower(player.getUniqueId()))),
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
                                bigDecimal.add(BigDecimal.valueOf(maxPlayerPower(player.getUniqueId()))),
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

    @Override
    public boolean addAlly(@NotNull DatabaseFaction faction) throws FactionIsFrozenException {
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
    public boolean addEnemy(@NotNull DatabaseFaction faction) throws FactionIsFrozenException {
        return !setStatus(faction, enemyId);
    }

    private boolean setStatus(@NotNull DatabaseFaction faction, int status) throws FactionIsFrozenException {
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
    public boolean resetRelation(@NotNull DatabaseFaction faction) throws FactionIsFrozenException {
        if (isFrozen()) throw new FactionIsFrozenException(registry);

        String registry = faction.getRegistry();

        database.evalTry("DELETE FROM faction_relations WHERE relation_registry_id = %s", registry)
                .get(PreparedStatement::executeUpdate);

        return true;
    }

    @Override
    public @NotNull FactionClaims<DatabaseFaction> getClaims() {
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
                InstantiationException | IllegalAccessException | InvocationTargetException ignored) {
            throw new SettingNotFoundException(setting);
        }
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
    public <C extends FactionModule<DatabaseFaction>>
    void createModule(@NotNull Class<C> clazz, Object... parameters)
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
