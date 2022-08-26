package io.github.toberocat.improvedFactions.core.player.data.local;

import io.github.toberocat.improvedFactions.core.player.data.PlayerDataHandler;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class LocalPlayerHandler extends PlayerDataHandler {

    private final HashMap<UUID, LocalPlayerSettings> loadedSettings = new HashMap<>();

    @Override
    public @NotNull LocalPlayerSettings getSettings(@NotNull UUID id) {
        return loadedSettings.computeIfAbsent(id, LocalPlayerSettings::new);
    }

    @Override
    public void leave(@NotNull UUID id) {
        loadedSettings.remove(id);
    }
}
