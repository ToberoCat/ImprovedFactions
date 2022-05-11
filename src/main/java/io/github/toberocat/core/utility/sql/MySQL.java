package io.github.toberocat.core.utility.sql;

import io.github.toberocat.core.utility.Utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {
    private final String host = "localhost";
    private final String port = "3306"; // 3308
    private final String database = "improved-factions";
    private final String username = "root";
    private final String password = "";
    private final boolean useSSL = false;

    private Connection connection;
    private SQL sql;

    public boolean isConnected() {
        return connection != null;
    }

    public void connect() throws SQLException, ClassNotFoundException {
        if (isConnected()) return;

        connection = DriverManager.getConnection("jdbc:mysql://" +
                        host + ":" + port + "/" + database + "?useSSL=" + useSSL,
                username, password);
        sql = new SQL(connection);
    }

    public void disconnect() {
        if (!isConnected()) return;

        try {
            connection.close();
        } catch (SQLException e) {
            Utility.except(e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public SQL getSql() {
        return sql;
    }
}
