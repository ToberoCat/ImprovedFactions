package io.github.toberocat.core.factions.database;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.factions.Description;
import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.OpenType;
import io.github.toberocat.core.factions.local.LocalFaction;
import io.github.toberocat.core.factions.claim.FactionClaims;
import io.github.toberocat.core.factions.local.rank.Rank;
import io.github.toberocat.core.factions.local.rank.members.OwnerRank;
import io.github.toberocat.core.utility.data.Table;
import io.github.toberocat.core.utility.data.database.DatabaseAccess;
import io.github.toberocat.core.utility.data.database.sql.MySqlDatabase;
import io.github.toberocat.core.utility.data.database.sql.SqlCode;
import io.github.toberocat.core.utility.data.database.sql.SqlVar;
import io.github.toberocat.core.utility.data.database.sql.builder.Select;
import io.github.toberocat.core.utility.date.DateCore;
import io.github.toberocat.core.utility.exceptions.DescriptionHasNoLine;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joda.time.LocalDateTime;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.UUID;

/**
 * This faction needs to sync up with the database none stop, so that there won't be any problems when data is changed by other
 * Plugin instances (BungeeCord, Velocity, Proxy stuff)
 *
 * To allow the best performance, these two implementations are seperated by different classes,
 * so that there are no unnecessary if statements to check if it should sync now or not
 */
public class DatabaseFaction implements Faction {

    private final MySqlDatabase database;
    private final DatabaseAccess access;

    private String registry;
    private String display;
    private String motd;
    private String tag;

    /**
     * Use FactionHandler#createFaction() to create a faction
     */
    public DatabaseFaction() {
        this.access = DatabaseAccess.accessPipeline(DatabaseAccess.class);
        this.database = access.database();
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
                SqlVar.of("openType", OpenType.valueOf(
                        config.getString("faction.default.openType", "INVITE_ONLY")).ordinal()),
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

    @Override
    public @NotNull String getRegistry() {
        return registry;
    }

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

    @Override
    public void setDisplay(@NotNull String display) {
        this.display = display;
        database
                .evalTry("UPDATE factions SET display_name = %s WHERE registry_id = %s",
                        display, registry)
                .get(PreparedStatement::executeUpdate);
    }

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

    @Override
    public void setMotd(@NotNull String motd) {
        this.motd = motd;
        database
                .evalTry("UPDATE factions SET motd = %s WHERE registry_id = %s",
                        motd, registry)
                .get(PreparedStatement::executeUpdate);
    }

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

    @Override
    public void setTag(@NotNull String tag) {
        this.tag = tag;
        database
                .evalTry("UPDATE factions SET tag = %s WHERE registry_id = %s",
                        tag, registry)
                .get(PreparedStatement::executeUpdate);
    }

    @Override
    public void setType(@NotNull OpenType type) {
        database
                .evalTry("UPDATE factions SET open_type = %d WHERE registry_id = %s", type.ordinal(), registry)
                .get(PreparedStatement::executeUpdate);
    }

    @Override
    public @NotNull Description getDescription() {
        return new Description() {
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
            public void setLine(int line, @NotNull String content) {
                if (hasLine(line)) database
                        .evalTry("UPDATE faction_descriptions SET content = %s WHERE registry_id = %s AND line = %s",
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
                .evalTry("UPDATE factions SET permanent = %b WHERE registry_id = %s", permanent, registry)
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
                .evalTry("UPDATE factions SET frozen = %b WHERE registry_id = %s", frozen, registry)
                .get(PreparedStatement::executeUpdate);
    }

    @Override
    public @NotNull Rank getPlayerRank(@Nullable OfflinePlayer player) {
        return null;
    }

    @Override
    public boolean hasPermission(@NotNull OfflinePlayer player, @NotNull String permission) {
        return false;
    }

    @Override
    public boolean isMember(@NotNull UUID player) {
        return database.rowSelect(new Select()
                .setTable(Table.PLAYERS.getTable())
                .setColumns("faction")
                .setFilter("uuid = %s", player.toString()))
                .readRow(String.class, "faction")
                .orElse("")
                .equals(registry);
    }

    @Override
    public void changeRank(@NotNull OfflinePlayer player, @NotNull Rank rank) {

    }

    @Override
    public void transferOwnership(@NotNull Player player) {

    }

    @Override
    public void deleteFaction() {

    }

    @Override
    public boolean joinPlayer(@NotNull Player player) {
        return joinPlayer(player, Rank.fromString(OwnerRank.registry));
    }

    @Override
    public boolean joinPlayer(@NotNull Player player, @NotNull Rank rank) {
        if (isMember(player.getUniqueId())) return false;

        database.evalTry("UPDATE players SET faction = %s WHERE uuid = %s",
                registry, player.getUniqueId().toString())
                .get(PreparedStatement::executeUpdate);
        database.evalTry("UPDATE players SET member_rank = %s WHERE uuid = %s",
                rank.getRegistryName(), player.getUniqueId().toString())
                .get(PreparedStatement::executeUpdate);

        return true;
    }

    @Override
    public boolean joinPlayer(@NotNull Player player, @NotNull UUID inviteId) {
        return false;
    }

    @Override
    public boolean leavePlayer(@NotNull Player player) {
        return false;
    }

    @Override
    public boolean kickPlayer(@NotNull OfflinePlayer player) {
        return false;
    }

    @Override
    public boolean banPlayer(@NotNull OfflinePlayer player) {
        return false;
    }

    @Override
    public boolean pardonPlayer(@NotNull OfflinePlayer player) {
        return false;
    }

    @Override
    public @NotNull BigDecimal getPower() {
        return null;
    }

    @Override
    public double playerPower(@NotNull OfflinePlayer player) {
        return 0;
    }

    @Override
    public boolean addAlly(@NotNull LocalFaction faction) {
        return false;
    }

    @Override
    public boolean addEnemy(@NotNull LocalFaction faction) {
        return false;
    }

    @Override
    public boolean resetRelation(@NotNull LocalFaction faction) {
        return false;
    }

    @Override
    public FactionClaims getClaims() {
        return null;
    }

    @Override
    public <C> @Nullable C getModule(@NotNull Class<C> clazz) {
        return null;
    }

    @Override
    public <C> void createModule(@NotNull Class<C> clazz, Object... parameters) {

    }
}
