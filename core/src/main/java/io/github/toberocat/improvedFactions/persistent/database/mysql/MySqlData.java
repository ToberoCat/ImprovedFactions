package io.github.toberocat.improvedFactions.persistent.database.mysql;

import io.github.toberocat.improvedFactions.database.MySqlDatabase;
import io.github.toberocat.improvedFactions.database.builder.Select;
import io.github.toberocat.improvedFactions.handler.DatabaseHandler;
import io.github.toberocat.improvedFactions.persistent.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class MySqlData implements Data {

    private final MySqlDatabase database;

    public MySqlData() {
        this.database = DatabaseHandler.api().getMySql();
    }


    @Override
    public @Nullable String set(@NotNull UUID id, @NotNull String key, @NotNull String value) {
        String previous = get(id, key);
        database.executeUpdate("INSERT INTO peristent_data VALUE (%s, %s, %s)",
                id.toString(), key, value);

        return previous;
    }

    @Override
    public @Nullable String remove(@NotNull UUID id, @NotNull String key) {
        String previous = get(id, key);
        database.executeUpdate("DELETE FROM persistent_data WHERE uuid = %s AND id = %s",
                id, key);

        return previous;
    }

    @Override
    public @Nullable String get(@NotNull UUID id, @NotNull String key) {
        return database.rowSelect(new Select()
                .setTable("persistent_data")
                .setColumns("value")
                .setFilter("uuid = %s, id = %s", id.toString(), key))
                .readRow(String.class, "value")
                .orElse(null);
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
