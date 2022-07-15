package io.github.toberocat.core.factions.database;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.factions.Description;
import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.OpenType;
import io.github.toberocat.core.factions.claim.FactionClaims;
import io.github.toberocat.core.factions.local.rank.Rank;
import io.github.toberocat.core.utility.data.Table;
import io.github.toberocat.core.utility.data.access.AbstractAccess;
import io.github.toberocat.core.utility.data.annotation.DatabaseField;
import io.github.toberocat.core.utility.data.database.DatabaseAccess;
import io.github.toberocat.core.utility.data.database.sql.builder.Insert;
import io.github.toberocat.core.utility.data.database.sql.builder.Select;
import io.github.toberocat.core.utility.exceptions.DescriptionHasNoLine;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.UUID;

/**
 * This faction needs to sync up with the database none stop, so that bungeecord / velocity can be used
 */
public class DatabaseFaction implements Faction {

    @DatabaseField
    private String registry;

    @DatabaseField
    private String display;

    @DatabaseField
    private String motd;

    @DatabaseField
    private String tag;

    @DatabaseField
    private String createdAt;


    public DatabaseFaction() {

    }

    /**
     * @param display This name isn't allowed to be longer than "faction.maxNameLen" in config.yml
     */
    public DatabaseFaction(@NotNull String display) {
        this.registry = Faction.displayToRegistry(display);
        this.display = display.substring(0, MainIF.config().getInt("faction.maxNameLen"));

        this.motd = MainIF.config().getString("faction.default.motd");

        DatabaseAccess.accessPipeline(DatabaseAccess.class).write(Table.FACTIONS, this);
    }

    @Override
    public @NotNull String getRegistry() {
        return registry;
    }

    @Override
    public @NotNull String getDisplay() {
        return DatabaseAccess.accessPipeline(DatabaseAccess.class)
                .database()
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
        DatabaseAccess.accessPipeline(DatabaseAccess.class)
                .database()
                .evalTry("UPDATE factions SET display_name = %s WHERE registry_id = %s",
                        display, registry)
                .get(PreparedStatement::executeUpdate);
    }

    @Override
    public @NotNull String getMotd() {
        return DatabaseAccess.accessPipeline(DatabaseAccess.class)
                .database()
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
        DatabaseAccess.accessPipeline(DatabaseAccess.class)
                .database()
                .evalTry("UPDATE factions SET motd = %s WHERE registry_id = %s",
                        motd, registry)
                .get(PreparedStatement::executeUpdate);
    }

    @Override
    public @NotNull String getTag() {
        return DatabaseAccess.accessPipeline(DatabaseAccess.class)
                .database()
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
        DatabaseAccess.accessPipeline(DatabaseAccess.class)
                .database()
                .evalTry("UPDATE factions SET tag = %s WHERE registry_id = %s",
                        tag, registry)
                .get(PreparedStatement::executeUpdate);
    }

    @Override
    public @NotNull Description getDescription() {
        return new Description() {
            @Override
            public @NotNull String getLine(int line) throws DescriptionHasNoLine {
                return DatabaseAccess.accessPipeline(DatabaseAccess.class)
                        .database()
                        .rowSelect(new Select()
                                .setTable(Table.FACTION_DESCRIPTIONS.getTable())
                                .setColumns("content")
                                .setFilter("registry_id = %s AND line = %d", registry, line))
                        .readRow(String.class, "content")
                        .orElse("");
            }

            @Override
            public void setLine(int line, @NotNull String content) {
                if (hasLine(line)) DatabaseAccess.accessPipeline(DatabaseAccess.class)
                        .database()
                        .evalTry("UPDATE faction_descriptions SET content = %s WHERE registry_id = %s AND line = %s",
                                content, registry, line)
                        .get(PreparedStatement::executeUpdate);
                else DatabaseAccess.accessPipeline(DatabaseAccess.class)
                        .database()
                        .evalTry("INSERT INTO faction_descriptions VALUE (%s, %d, %s)",
                                registry, line, content)
                        .get(PreparedStatement::executeUpdate);
            }

            @Override
            public boolean hasLine(int line) {
                return DatabaseAccess.accessPipeline(DatabaseAccess.class)
                        .database()
                        .rowSelect(new Select()
                                .setTable(Table.FACTION_DESCRIPTIONS.getTable())
                                .setColumns("content")
                                .setFilter("registry_id = %s AND line = %d", registry, line))
                        .readRow(String.class, "content")
                        .isPresent();
            }

            @Override
            public int getLastLine() {
                return DatabaseAccess.accessPipeline(DatabaseAccess.class)
                        .database()
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
        return DatabaseAccess.accessPipeline(DatabaseAccess.class)
                .database()
                .rowSelect(new Select()
                        .setTable(Table.FACTIONS.getTable())
                        .setColumns("created_at")
                        .setFilter("registry_id = %s", registry))
                .readRow(String.class, "created_at")
                .orElse(createdAt);
    }

    @Override
    public @NotNull OpenType getType() {
        return DatabaseAccess.accessPipeline(DatabaseAccess.class)
                .database()
                .rowSelect(new Select()
                        .setTable(Table.FACTIONS.getTable())
                        .setColumns("open_type")
                        .setFilter("registry_id = %s", registry))
                .readRow(Integer.class, "open_type")
                .orElse(createdAt);
    }

    @Override
    public void setType(@NotNull OpenType type) {

    }

    @Override
    public boolean isPermanent() {
        return false;
    }

    @Override
    public void setPermanent(boolean permanent) {

    }

    @Override
    public boolean isFrozen() {
        return false;
    }

    @Override
    public void setFrozen(boolean frozen) {

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
        return false;
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
        return false;
    }

    @Override
    public boolean joinPlayer(@NotNull Player player, @NotNull Rank rank) {
        return false;
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
    public boolean addAlly(@NotNull Faction faction) {
        return false;
    }

    @Override
    public boolean addEnemy(@NotNull Faction faction) {
        return false;
    }

    @Override
    public boolean resetRelation(@NotNull Faction faction) {
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
