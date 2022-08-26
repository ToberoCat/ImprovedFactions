package io.github.toberocat.improvedFactions.core.player.data;

import io.github.toberocat.improvedFactions.core.handler.ConfigHandler;
import io.github.toberocat.improvedFactions.core.player.data.local.LocalPlayerSettings;
import io.github.toberocat.improvedFactions.core.player.data.database.sql.MySqlPlayerHandler;
import io.github.toberocat.improvedFactions.core.player.data.local.LocalPlayerHandler;
import io.github.toberocat.improvedFactions.core.setting.SettingHolder;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public abstract class PlayerDataHandler {
    private static final PlayerDataHandler implementation = create();

    private static PlayerDataHandler create() {
        if (ConfigHandler.api().getBool("storage.use-mysql", false))
            return new MySqlPlayerHandler();
        return new LocalPlayerHandler();
    }

    public abstract @NotNull SettingHolder getSettings(@NotNull UUID id);

    public void leave(@NotNull UUID id) {

    }
}
