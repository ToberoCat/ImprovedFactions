package io.github.toberocat.core.utility.sql;

import io.github.toberocat.core.utility.callbacks.TryRunnable;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySql {
    private final String database;
    private final String address;

    private final SqlHandler handler;

    private Connection connection;

    public MySql(@NotNull String host, int port, @NotNull String database) {
        this.database = database;
        this.address = host + ":" + port;
        this.handler = new SqlHandler(this);
    }

    public boolean isConnected() {
        return connection == null;
    }

    public void login(@NotNull String user, @NotNull String password) throws ClassNotFoundException, SQLException {
        if (isConnected()) return;

        connection = DriverManager.getConnection("jdbc:mysql://" + address + "/"
                + database + "?useSSL=false", user, password);
        handler.createSchema(database);
    }

    public void disconnect() throws SQLException {
        if (!isConnected()) return;
        connection.close();
        connection = null;
    }

    public PreparedStatement eval(@NotNull String sql, Object... args) throws SQLException {
        if (!isConnected()) return null;
        return connection.prepareStatement(String.format(sql, args));
    }

    public TryRunnable<PreparedStatement> evalTry(@NotNull String sql, Object... args) {
        if (!isConnected()) return null;

        return new TryRunnable<>() {
            @Override
            protected PreparedStatement execute() throws Exception {
                return connection.prepareStatement(String.format(sql, args));
            }
        };
    }

    public SqlHandler getHandler() {
        return handler;
    }
}
