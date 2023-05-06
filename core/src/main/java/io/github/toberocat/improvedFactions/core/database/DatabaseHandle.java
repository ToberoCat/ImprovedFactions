package io.github.toberocat.improvedFactions.core.database;

import io.github.toberocat.improvedFactions.core.database.mysql.MySqlDatabase;
import io.github.toberocat.improvedFactions.core.exceptions.database.CantRequestDatabaseRuntimeException;
import io.github.toberocat.improvedFactions.core.handler.ConfigFile;
import org.jetbrains.annotations.NotNull;

public final class DatabaseHandle {

    private static MySqlDatabase mySqlDatabase;

    public static @NotNull MySqlDatabase requestMySql() {
        ConfigFile config = ConfigFile.api();
        if (!config.getBool("storage.use-mysql", false))
            throw new CantRequestDatabaseRuntimeException("MySQL");

        if (mySqlDatabase == null) mySqlDatabase = new MySqlDatabase(
                config.getString("storage.mysql.host", "localhost"),
                config.getString("storage.mysql.user", "root"),
                config.getString("storage.mysql.password", "1234"),
                "improved_factions",
                config.getInt("storage.mysql.port", 3306)
        );

        return mySqlDatabase;
    }
}
