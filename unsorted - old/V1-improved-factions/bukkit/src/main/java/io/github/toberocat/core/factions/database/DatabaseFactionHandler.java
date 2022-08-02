package io.github.toberocat.core.factions.database;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.handler.FactionHandlerInterface;
import io.github.toberocat.core.factions.components.rank.GuestRank;
import io.github.toberocat.core.factions.components.rank.Rank;
import io.github.toberocat.core.utility.data.Table;
import io.github.toberocat.core.utility.data.database.DatabaseAccess;
import io.github.toberocat.core.utility.data.database.sql.MySqlDatabase;
import io.github.toberocat.core.utility.data.database.sql.builder.Select;
import io.github.toberocat.core.utility.exceptions.faction.FactionNotInStorage;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class DatabaseFactionHandler implements FactionHandlerInterface<DatabaseFaction> {
    private static DatabaseFactionHandler instance;

    private final Map<String, DatabaseFaction> factions = new HashMap<>();
    private final MySqlDatabase database;
    private final DatabaseAccess access;

    public DatabaseFactionHandler() {
        this.access = DatabaseAccess.accessPipeline(DatabaseAccess.class);
        this.database = access.database();

        instance = this;
    }

    @Override
    public @NotNull DatabaseFaction create(@NotNull String display, @NotNull Player owner) {
        return new DatabaseFaction(display, owner);
    }

    @Override
    public @NotNull DatabaseFaction load(@NotNull String registry) throws FactionNotInStorage {
        return null;
    }

    @Override
    public boolean isLoaded(@NotNull String registry) {
        return false;
    }

    @Override
    public boolean exists(@NotNull String registry) {
        return false;
    }

    @Override
    public @NotNull Map<String, DatabaseFaction> getLoadedFactions() {
        return factions;
    }

    @Override
    public @NotNull Stream<String> getAllFactions() {
        return null;
    }

    @Override
    public void deleteCache(@NotNull String registry) {

    }

    @Override
    public @NotNull Rank getSavedRank(@NotNull OfflinePlayer player) {
        return Rank.fromString(database.rowSelect(new Select()
                        .setTable(Table.PLAYERS.getTable())
                        .setColumns("member_rank")
                        .setFilter("uuid = %s", player.getUniqueId().toString()))
                .readRow(String.class, "member_rank")
                .orElse(GuestRank.register));
    }

    @Override
    public @Nullable String getPlayerFaction(@NotNull OfflinePlayer player) {
        return database
                .rowSelect(new Select()
                        .setTable(Table.PLAYERS.getTable())
                        .setColumns("faction")
                        .setFilter("uuid = %s", player.getUniqueId()))
                .readRow(String.class, "faction")
                .orElse(null);
    }

    @Override
    public @Nullable String getPlayerFaction(@NotNull Player player) {
        return null;
    }

    @Override
    public boolean isInFaction(@NotNull OfflinePlayer player) {
        return false;
    }

    @Override
    public boolean isInFaction(@NotNull Player player) {
        return false;
    }

    /**
     * The faction cache is responsible for quick access of factions for players.
     * But if the faction gets deleted, this cache needs to get removed, else it will
     * wrongly display commands and crash the system trying to load the not existing faction
     *
     * @param player The player that should get the faction cache removed
     */
    @Override
    public void removeFactionCache(@NotNull Player player) {

    }

    /**
     * Gives the current handler instance.
     * Can be null, because it only got instanced when a mysql got loaded
     * @return This instance of the database handler
     */
    public static @Nullable DatabaseFactionHandler getInstance() {
        return instance;
    }
}
