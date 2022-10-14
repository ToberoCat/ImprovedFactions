package io.github.toberocat.improvedFactions.core.player.data.local;

import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.player.data.PlayerDataHandler;
import io.github.toberocat.improvedFactions.core.utils.FileAccess;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class LocalPlayerHandler extends PlayerDataHandler {

    private final HashMap<UUID, LocalPlayerSettings> loadedSettings = new HashMap<>();
    private final FileAccess access;

    public LocalPlayerHandler() {
        access = new FileAccess(ImprovedFactions.api().getDataFolder(),
                FileAccess.PLAYERS_FOLDER);
    }

    @Override
    public @NotNull LocalPlayerSettings getSettings(@NotNull UUID id) {
        return loadedSettings.computeIfAbsent(id, x -> new LocalPlayerSettings(access, x));
    }

    @Override
    public void leave(@NotNull UUID id) {
        LocalPlayerSettings settings = loadedSettings.remove(id);
        if (settings == null) return;

        try {
            access.write(settings, id + ".json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
