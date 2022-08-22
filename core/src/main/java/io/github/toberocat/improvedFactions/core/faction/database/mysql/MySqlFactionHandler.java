package io.github.toberocat.improvedFactions.core.faction.database.mysql;

import io.github.toberocat.improvedFactions.core.database.DatabaseHandle;
import io.github.toberocat.improvedFactions.core.database.mysql.MySqlDatabase;
import io.github.toberocat.improvedFactions.core.database.mysql.builder.Select;
import io.github.toberocat.improvedFactions.core.event.EventExecutor;
import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionAlreadyExistsException;
import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.core.exceptions.faction.IllegalFactionNamingException;
import io.github.toberocat.improvedFactions.core.faction.components.rank.GuestRank;
import io.github.toberocat.improvedFactions.core.faction.components.rank.Rank;
import io.github.toberocat.improvedFactions.core.faction.components.rank.members.FactionRank;
import io.github.toberocat.improvedFactions.core.faction.handler.FactionHandlerInterface;
import io.github.toberocat.improvedFactions.core.sender.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.sender.player.OfflineFactionPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

// ToDo: Add functionality to implemented methods
public class MySqlFactionHandler implements FactionHandlerInterface<MySqlFaction> {
    private static MySqlFactionHandler instance;

    private final Map<String, MySqlFaction> factions = new HashMap<>();
    private final MySqlDatabase database;

    public MySqlFactionHandler() {
        this.database = DatabaseHandle.requestMySql();

        instance = this;
    }

    /**
     * Gives the current handler instance.
     * Can be null, because it only got instanced when a mysql got loaded
     *
     * @return This instance of the database handler
     */
    public static @Nullable MySqlFactionHandler getInstance() {
        return instance;
    }

    @Override
    public @NotNull MySqlFaction create(@NotNull String display, @NotNull FactionPlayer<?> owner)
            throws IllegalFactionNamingException, FactionAlreadyExistsException {

        MySqlFaction faction = new MySqlFaction(display, owner);
        EventExecutor.getExecutor().createFaction(faction, owner);
        return faction;
    }

    @Override
    public @NotNull MySqlFaction load(@NotNull String registry) throws FactionNotInStorage {
        int size = database.rowSelect(new Select()
                        .setTable("factions")
                        .setColumns("")
                        .setFilter("registry = %s", registry))
                .getRows()
                .size();
        if (size != 1) throw new FactionNotInStorage(registry, FactionNotInStorage.StorageType.DATABASE);
        return new MySqlFaction(registry);
    }

    @Override
    public boolean isLoaded(@NotNull String registry) {
        return factions.containsKey(registry);
    }

    @Override
    public boolean exists(@NotNull String registry) {
        if (isLoaded(registry)) return true;
        return database.rowSelect(new Select()
                        .setTable("factions")
                        .setColumns("")
                        .setFilter("registry = %s", registry))
                .getRows()
                .size() == 1;
    }

    @Override
    public @NotNull Map<String, MySqlFaction> getLoadedFactions() {
        return factions;
    }

    @Override
    public @NotNull Stream<String> getAllFactions() {
        return database.rowSelect(new Select()
                .setTable("factions")
                .setColumns("registry"))
                .getRows()
                .stream()
                .map(x -> x.get("registry").toString());
    }

    @Override
    public void deleteCache(@NotNull String registry) {
        factions.remove(registry);
    }

    @Override
    public @NotNull FactionRank getSavedRank(@NotNull OfflineFactionPlayer<?> player) {
        return (FactionRank) Rank.fromString(database.rowSelect(new Select()
                        .setTable("players")
                        .setColumns("member_rank")
                        .setFilter("uuid = %s", player.getUniqueId().toString()))
                .readRow(String.class, "member_rank")
                .orElse(GuestRank.REGISTRY));
    }
}
