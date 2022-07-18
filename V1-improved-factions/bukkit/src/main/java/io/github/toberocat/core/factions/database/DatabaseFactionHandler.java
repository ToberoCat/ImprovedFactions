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
    public @NotNull Faction create(@NotNull String display, @NotNull Player owner) {
        return new DatabaseFaction(display, owner);
    }

    @Override
    public @NotNull Faction load(@NotNull String registry) throws FactionNotInStorage {
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
    public @NotNull Rank getSavedRank(@NotNull OfflinePlayer player) {
        return Rank.fromString(database.rowSelect(new Select()
                        .setTable(Table.PLAYERS.getTable())
                        .setColumns("member_rank")
                        .setFilter("uuid = %s", player.getUniqueId().toString()))
                .readRow(String.class, "member_rank")
                .orElse(GuestRank.register));
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
