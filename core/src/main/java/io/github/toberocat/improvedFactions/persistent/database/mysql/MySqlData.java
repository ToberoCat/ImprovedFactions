package io.github.toberocat.improvedFactions.persistent.database.mysql;

import io.github.toberocat.improvedFactions.database.MySqlDatabase;
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
        return null;
    }

    @Override
    public @Nullable String get(@NotNull UUID id, @NotNull String key) {
        return null;
    }

    @Override
    public boolean has(@NotNull UUID id, @NotNull String key) {
        return false;
    }
}
