package io.github.toberocat.improvedFactions.core.player.data.database.sql;

import io.github.toberocat.improvedFactions.core.database.DatabaseHandle;
import io.github.toberocat.improvedFactions.core.database.mysql.MySqlDatabase;
import io.github.toberocat.improvedFactions.core.database.mysql.builder.Select;
import io.github.toberocat.improvedFactions.core.exceptions.setting.ErrorParsingSettingException;
import io.github.toberocat.improvedFactions.core.player.data.PlayerDataHandler;
import io.github.toberocat.improvedFactions.core.setting.Setting;
import io.github.toberocat.improvedFactions.core.setting.Settings;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.stream.Stream;

public class MySqlPlayerHandler extends PlayerDataHandler {

    private final MySqlDatabase database;

    public MySqlPlayerHandler() {
        database = DatabaseHandle.requestMySql();
    }

    @Override
    public @NotNull Settings getSettings(@NotNull UUID id) {
        return new Settings() {
            @Override
            public <T> void setSetting(@NotNull Setting<T> setting, T value) {
                database.executeUpdate("INSERT INTO player_settings VALUE (%s, %s, %s)",
                        id.toString(), setting.label(), setting.toSave(value));
            }

            @Override
            public <T> @NotNull T getSetting(@NotNull Setting<T> setting) throws ErrorParsingSettingException {
                return setting.createFromSave(database.rowSelect(new Select()
                                .setTable("player_settings")
                                .setColumns("value")
                                .setFilter("uuid = %s", id.toString()))
                        .readRow(String.class, "value")
                        .orElseThrow(ErrorParsingSettingException::new));
            }

            @Override
            public @NotNull Stream<Setting<?>> listSettings() {
                return database.rowSelect(new Select()
                                .setTable("player_settings")
                                .setColumns("setting")
                                .setFilter("uuid = %s", id.toString()))
                        .getRows()
                        .stream()
                        .map(Object::toString)
                        .map(Setting::getSetting);
            }
        };
    }
}
