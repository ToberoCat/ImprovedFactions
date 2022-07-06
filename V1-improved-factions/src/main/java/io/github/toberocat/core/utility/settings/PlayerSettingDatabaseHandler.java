package io.github.toberocat.core.utility.settings;

import io.github.toberocat.core.debug.Debugger;
import io.github.toberocat.core.utility.data.DataAccess;
import io.github.toberocat.core.utility.settings.type.Setting;
import io.github.toberocat.core.utility.sql.MySql;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PlayerSettingDatabaseHandler {

    private final MySql sql;
    private final UUID uuid;

    public PlayerSettingDatabaseHandler(@NotNull MySql sql, @NotNull UUID player) {
        this.sql = sql;
        this.uuid = player;
    }

    public PlayerSettings createSettings() {
        PlayerSettings settings = DataAccess.isSql() ? readSettingsMsql() :
                readSettingsJson();

        if (settings == null || settings.getPlayerSetting() == null) {
            Debugger.log("Couldn't load player settings. Creating new one");
            settings = new PlayerSettings(uuid);
        } else {
            Debugger.log("Loaded player settings for " +
                    Bukkit.getOfflinePlayer(uuid).getName());
            Setting.populateSettings(PlayerSettings.DEFAULT_SETTINGS,
                    settings.getPlayerSetting());
        }

        return settings;
    }

    private PlayerSettings readSettingsMsql() {
        if (!isPlayerSaved()) return null;

        return new PlayerSettings().read(sql);
    }

    private boolean isPlayerSaved() {
        try {
            PreparedStatement ps = sql.eval("SELECT * FROM player_settings WHERE uuid = %s",
                    uuid.toString());
            ResultSet set = ps.executeQuery();
            return set.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private PlayerSettings readSettingsJson() {
        String player = uuid.toString();
        if (!DataAccess.existsFolder("Players", player)) return null;
        try {
            return DataAccess.getWithExceptions("Players", player, PlayerSettings.class);
        } catch (IOException | NoSuchMethodException | InvocationTargetException
                | InstantiationException | IllegalAccessException ignored) {}

        return null;
    }
}
