package io.github.toberocat.improvedFactions.core.faction.local;

import io.github.toberocat.improvedFactions.core.event.EventExecutor;
import io.github.toberocat.improvedFactions.core.exceptions.faction.*;
import io.github.toberocat.improvedFactions.core.faction.handler.FactionHandlerInterface;
import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.persistent.PersistentHandler;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.utils.FileAccess;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

// ToDo: Implement everything
public class LocalFactionHandler implements FactionHandlerInterface<LocalFaction> {
    private static LocalFactionHandler instance;
    private final Map<String, LocalFaction> factions;
    private final FileAccess access;

    public LocalFactionHandler() {
        factions = new HashMap<>();
        access = new FileAccess(ImprovedFactions.api().getLocalDataFolder());

        instance = this;
    }

    public static @Nullable LocalFactionHandler getInstance() {
        return instance;
    }

    @Override
    public @NotNull LocalFaction create(@NotNull String display, @NotNull FactionPlayer<?> owner)
            throws FactionIsFrozenException,
            PlayerIsAlreadyInFactionException,
            PlayerIsBannedException, IllegalFactionNamingException, FactionAlreadyExistsException {
        LocalFaction faction = new LocalFaction(display, owner);
        owner.getDataContainer().set(PersistentHandler.FACTION_KEY, faction.getRegistry());

        factions.put(faction.getRegistry(), faction);
        EventExecutor.getExecutor().createFaction(faction, owner);

        try {
            access.write(faction.toDataType(),
                    FileAccess.FACTION_FOLDER,
                    faction.getRegistry() + ".json");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return faction;
    }

    @Override
    public @NotNull LocalFaction load(@NotNull String registry) throws FactionNotInStorage {
        if (!access.has(FileAccess.FACTION_FOLDER, registry + ".json")) throw
                new FactionNotInStorage(registry, FactionNotInStorage.StorageType.LOCAL_FILE);

        try {
            return new LocalFaction(access.read(LocalFactionDataType.class,
                    FileAccess.FACTION_FOLDER, registry + ".json"));
        } catch (IOException e) {
            throw new FactionNotInStorage(registry, FactionNotInStorage.StorageType.LOCAL_FILE);
        }
    }

    @Override
    public boolean isLoaded(@NotNull String registry) {
        return factions.containsKey(registry);
    }

    @Override
    public boolean exists(@NotNull String registry) {
        return access.has(FileAccess.FACTION_FOLDER, registry);
    }

    @Override
    public @NotNull Map<String, LocalFaction> getLoadedFactions() {
        return factions;
    }

    @Override
    public @NotNull Stream<String> getAllFactions() {
        return Stream.concat(
                        Arrays.stream(access.list(FileAccess.FACTION_FOLDER)),
                        factions.keySet().stream()
                )
                .map(x -> x.split("\\.")[0])
                .distinct();
    }

    @Override
    public void deleteCache(@NotNull String registry) {
        LocalFaction faction = factions.remove(registry);
        if (faction == null) return;

        try {
            access.write(faction.toDataType(),
                    FileAccess.FACTION_FOLDER, registry + ".json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteFromFile(@NotNull String registry) {
        factions.remove(registry);
        access.getFile(FileAccess.FACTION_FOLDER, registry + ".json").delete();
    }
}
