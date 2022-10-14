package io.github.toberocat.improvedFactions.core.persistent.database.mysql;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.toberocat.improvedFactions.core.database.DatabaseHandle;
import io.github.toberocat.improvedFactions.core.database.mysql.MySqlDatabase;
import io.github.toberocat.improvedFactions.core.database.mysql.builder.Select;
import io.github.toberocat.improvedFactions.core.json.Json;
import io.github.toberocat.improvedFactions.core.persistent.PersistentData;
import io.github.toberocat.improvedFactions.core.utils.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class MySqlData implements PersistentData {

    private final MySqlDatabase database;

    public MySqlData() {
        database = DatabaseHandle.requestMySql();
    }


    @Override
    public @Nullable Object set(@NotNull UUID id, @NotNull String key, @NotNull Object value) {
        Object previous = get(id, key);
        try {
            database.executeUpdate("INSERT INTO peristent_data VALUE (%s, %s, %s)",
                    id.toString(), key, Json.parse(value));
        } catch (JsonProcessingException e) {
            Logger.api().logException(e);
        }

        return previous;
    }

    @Override
    public @Nullable Object remove(@NotNull UUID id, @NotNull String key) {
        Object previous = get(id, key);
        database.executeUpdate("DELETE FROM persistent_data WHERE uuid = %s AND id = %s",
                id, key);

        return previous;
    }

    @Override
    public @Nullable Object get(@NotNull UUID id, @NotNull String key) {
        try {
            return Json.parse(Object.class, database.rowSelect(new Select()
                    .setTable("persistent_data")
                    .setColumns("value")
                    .setFilter("uuid = %s, id = %s", id.toString(), key))
                    .readRow(String.class, "value")
                    .orElse(null));
        } catch (JsonProcessingException e) {
            Logger.api().logException(e);
            return null;
        }
    }

    @Override
    public boolean has(@NotNull UUID id, @NotNull String key) {
        return database.rowSelect(new Select()
                        .setTable("persistent_data")
                        .setColumns("value")
                        .setFilter("uuid = %s, id = %s", id.toString(), key))
                .hasItem()
                .isPresent();
    }
}
