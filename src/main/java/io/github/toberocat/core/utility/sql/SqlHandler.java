package io.github.toberocat.core.utility.sql;

import io.github.toberocat.core.utility.Utility;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class SqlHandler {
    private final MySql database;

    public SqlHandler(@NotNull MySql database) {
        this.database = database;
    }

    public void createSchema(@NotNull String schema) {
        try {
            database.eval("CREATE SCHEMA IF NOT EXISTS %s", schema);
        } catch (SQLException e) {
            Utility.except(e);
        }
    }

    public void createTableIfNotExist(@NotNull String sql) {
        try {
            database.eval("CREATE TABLE IF NOT EXISTS %s", sql);
        } catch (SQLException e) {
            Utility.except(e);
        }
    }
}
