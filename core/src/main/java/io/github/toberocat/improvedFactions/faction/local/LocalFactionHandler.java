package io.github.toberocat.improvedFactions.faction.local;

import io.github.toberocat.core.factions.components.rank.Rank;
import io.github.toberocat.core.factions.handler.FactionHandlerInterface;
import io.github.toberocat.core.utility.data.Table;
import io.github.toberocat.core.utility.data.access.FileAccess;
import io.github.toberocat.core.utility.exceptions.faction.FactionNotInStorage;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class LocalFactionHandler implements FactionHandlerInterface<LocalFaction> {
    private final Map<String, LocalFaction> factions;
    private final FileAccess access;

    public LocalFactionHandler() {
        this.factions = new HashMap<>();
        access = FileAccess.accessPipeline(FileAccess.class);
    }

    @Override
    public @NotNull LocalFaction create(@NotNull String display, @NotNull Player owner) {
        LocalFaction faction = new LocalFaction(display, owner);
        factions.put(faction.getRegistry(), faction);

        return faction;
    }

    @Override
    public @NotNull LocalFaction load(@NotNull String registry) throws FactionNotInStorage {
        if (!access.has(Table.FACTIONS, registry)) throw
                new FactionNotInStorage(registry, FactionNotInStorage.StorageType.LOCAL_FILE);

        return access.read(Table.FACTIONS, registry);
    }

    @Override
    public boolean isLoaded(@NotNull String registry) {
        return factions.containsKey(registry);
    }

    @Override
    public boolean exists(@NotNull String registry) {
        return access.has(Table.FACTIONS, registry);
    }

    @Override
    public @NotNull Map<String, LocalFaction> getLoadedFactions() {
        return factions;
    }

    @Override
    public @NotNull Stream<String> getAllFactions() {
        return access.listInTableStream(Table.FACTIONS);
    }

    @Override
    public void deleteCache(@NotNull String registry) {
        factions.remove(registry);
    }

    @Override
    public @NotNull Rank getSavedRank(@NotNull OfflinePlayer player) {
        return null;
    }

    @Override
    public @Nullable String getPlayerFaction(@NotNull OfflinePlayer player) {
        return null;
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
}
