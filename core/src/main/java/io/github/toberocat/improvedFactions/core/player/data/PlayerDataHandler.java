package io.github.toberocat.improvedFactions.core.player.data;

import io.github.toberocat.improvedFactions.core.handler.ConfigFile;
import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.player.data.database.sql.MySqlPlayerHandler;
import io.github.toberocat.improvedFactions.core.player.data.local.LocalPlayerHandler;
import io.github.toberocat.improvedFactions.core.setting.Settings;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public abstract class PlayerDataHandler {
    private static final PlayerDataHandler implementation = create();

    private static PlayerDataHandler create() {
        if (ImprovedFactions.api().getConfig().getBool("storage.use-mysql", false))
            return new MySqlPlayerHandler();
        return new LocalPlayerHandler();
    }

    public abstract @NotNull Settings getSettings(@NotNull UUID id);

    public void leave(@NotNull UUID id) {

    }
}
