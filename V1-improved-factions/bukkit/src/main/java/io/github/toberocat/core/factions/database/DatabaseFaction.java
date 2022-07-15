package io.github.toberocat.core.factions.database;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.factions.Description;
import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.OpenType;
import io.github.toberocat.core.factions.claim.FactionClaims;
import io.github.toberocat.core.factions.local.rank.Rank;
import io.github.toberocat.core.utility.data.Table;
import io.github.toberocat.core.utility.data.annotation.DatabaseField;
import io.github.toberocat.core.utility.data.database.DatabaseAccess;
import io.github.toberocat.core.utility.data.database.sql.builder.Select;
import io.github.toberocat.core.utility.data.database.sql.builder.Update;
import io.github.toberocat.core.utility.data.database.sql.builder.UpdateValue;
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
    private final String registry;
    private String display;

    /**
     * @param display This name isn't allowed to be longer than "faction.maxNameLen" in config.yml
     */
    public DatabaseFaction(@NotNull String display) {
        this.registry = Faction.displayToRegistry(display);
        this.display = display.substring(0, MainIF.config().getInt("faction.maxNameLen"));
    }

    @Override
    public @NotNull String getRegistry() {
        return registry;
    }

    @Override
    public @NotNull String getDisplay() {
        return DatabaseAccess.accessPipeline(DatabaseAccess.class)
                .database()
                .item()
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
                .item()
                .evalTry("UPDATE factions SET display_name = %s WHERE registry_id = %s",
                        display, registry)
                .get(PreparedStatement::executeUpdate);
    }

    @Override
    public @NotNull String getMotd() {
        return null;
    }

    @Override
    public void setMotd(@NotNull String motd) {

    }

    @Override
    public @NotNull String getTag() {
        return null;
    }

    @Override
    public void setTag(@NotNull String tag) {

    }

    @Override
    public @NotNull Description getDescription() {
        return null;
    }

    @Override
    public @NotNull String getCreatedAt() {
        return null;
    }

    @Override
    public @NotNull OpenType getType() {
        return null;
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
