package io.github.toberocat.improvedFactions.core.faction.database.mysql;

import io.github.toberocat.improvedFactions.core.database.DatabaseHandle;
import io.github.toberocat.improvedFactions.core.database.mysql.MySqlDatabase;
import io.github.toberocat.improvedFactions.core.database.mysql.builder.Select;
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
        return new MySqlFaction(display, owner);
    }

    @Override
    public @NotNull MySqlFaction load(@NotNull String registry) throws FactionNotInStorage {
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
    public @NotNull Map<String, MySqlFaction> getLoadedFactions() {
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
    public @NotNull FactionRank getSavedRank(@NotNull OfflineFactionPlayer<?> player) {
        return (FactionRank) Rank.fromString(database.rowSelect(new Select()
                        .setTable("players")
                        .setColumns("member_rank")
                        .setFilter("uuid = %s", player.getUniqueId().toString()))
                .readRow(String.class, "member_rank")
                .orElse(GuestRank.REGISTRY));
    }

    @Override
    public @Nullable String getPlayerFaction(@NotNull OfflineFactionPlayer<?> player) {
        return database
                .rowSelect(new Select()
                        .setTable("players")
                        .setColumns("faction")
                        .setFilter("uuid = %s", player.getUniqueId()))
                .readRow(String.class, "faction")
                .orElse(null);
    }

    @Override
    public @Nullable String getPlayerFaction(@NotNull FactionPlayer<?> player) {
        return null;
    }

    @Override
    public boolean isInFaction(@NotNull OfflineFactionPlayer<?> player) {
        return false;
    }

    @Override
    public boolean isInFaction(@NotNull FactionPlayer<?> player) {
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
    public void removeFactionCache(@NotNull FactionPlayer<?> player) {

    }
}
