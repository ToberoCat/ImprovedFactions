package io.github.toberocat.core.utility.data.database.sql;

import com.mysql.cj.jdbc.MysqlDataSource;
import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.callbacks.TryRunnable;
import io.github.toberocat.core.utility.data.Database;
import io.github.toberocat.core.utility.data.database.sql.builder.*;
import org.jetbrains.annotations.NotNull;

import java.sql.*;

public class MySqlDatabase extends Database {

    private final String host, user, password, db;
    private final int port;
    private Connection connection;

    public MySqlDatabase(@NotNull String host,
                         @NotNull String user,
                         @NotNull String pw,
                         @NotNull String db,
                         int port) {
        this.host = host;
        this.user = user;
        this.password = pw;
        this.db = db;
        this.port = port;
    }

    // Connection
    public boolean connect() {
        try {
            MysqlDataSource dataSource = new MysqlDataSource();

            dataSource.setServerName(host);
            dataSource.setPort(port);
            dataSource.setDatabaseName(db);
            dataSource.setUser(user);
            dataSource.setPassword(password);
            dataSource.setAllowMultiQueries(true);
            connection = dataSource.getConnection();
            return true;
        } catch (SQLException e) {
            MainIF.getIF().saveShutdown("&cDatabase not connected. " +
                    "Please check if your login information, host and port are right");
            return false;
        }
    }

    public boolean isConnected() {
        if (connection == null) {
            return false;
        }
        try {
            if (connection.isClosed()) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean close() {
        try {
            connection.close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    // Database Interaction
    public boolean tableInsert(String table, String columns, String... data) {
        StringBuilder sqldata = new StringBuilder();
        int i = 0;
        for (String d : data) {
            sqldata.append("'").append(d).append("'");
            i++;
            if (i != data.length) {
                sqldata.append(", ");
            }
        }

        String sql = "INSERT INTO " + table + " (" + columns + ") VALUES (" + sqldata + ");";
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            stmt.execute(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public boolean tableInsert(Insert... builders) {
        StringBuilder sql = new StringBuilder();
        for (Insert b : builders) {
            StringBuilder sqldata = new StringBuilder();
            int i = 0;
            for (String d : b.getData()) {
                sqldata.append("'").append(d).append("'");
                i++;
                if (i != b.getData().length) {
                    sqldata.append(", ");
                }
            }


            sql.append("INSERT INTO ").append(b.getTable()).append(" (").append(b.getColumns()).append(") VALUES (").append(sqldata).append("); ");

        }
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            stmt.execute(sql.toString());

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public boolean rowUpdate(String table, UpdateValue value, String filter) {
        StringBuilder change = new StringBuilder();
        int i = 0;
        for (String key : value.getKeys()) {
            change.append(key).append(" = '").append(value.get(key)).append("'");
            i++;
            if (i != value.getKeys().size()) {
                change.append(", ");
            }
        }
        String sql = "UPDATE " + table + " SET " + change + " WHERE " + filter + ";";
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public boolean rowUpdate(Update... builders) {
        StringBuilder sql = new StringBuilder();
        for (Update u : builders) {
            StringBuilder change = new StringBuilder();
            int i = 0;
            for (String key : u.getValue().getKeys()) {
                change.append(key).append(" = '").append(u.getValue().get(key)).append("'");
                i++;
                if (i != u.getValue().getKeys().size()) {
                    change.append(", ");
                }
            }
            sql.append("UPDATE ").append(u.getTable()).append(" SET ").append(change).append(" WHERE ").append(u.getFilter()).append("; ");
        }
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            stmt.execute(sql.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public Result rowSelect(String table, String columns, String filter) {
        if (columns == null || columns.equals("")) {
            columns = "*";
        }
        String sql = "SELECT " + columns + " FROM " + table;
        if (filter != null && !filter.equals("")) {
            sql = sql + " WHERE " + filter;
        }
        sql = sql + ";";

        Statement stmt;
        ResultSet res;
        try {
            stmt = connection.createStatement();
            res = stmt.executeQuery(sql);
            ResultSetMetaData resmeta = res.getMetaData();
            Result result = new Result();
            while (res.next()) {
                Row row = new Row();
                int i = 1;
                boolean bound = true;
                while (bound) {
                    try {
                        row.addColumn(resmeta.getColumnName(i), res.getObject(i));
                    } catch (SQLException e) {
                        bound = false;
                    }

                    i++;
                }
                result.addRow(row);
            }
            return result;

        } catch (SQLException e) {
            e.printStackTrace();
            return new Result();
        }
    }

    public Result rowSelect(Select s) {
        String sql = "";
        String columns;
        String lsql;
        if (s.getColumns() == null || s.getColumns().equals("")) {
            columns = "*";
        } else {
            columns = s.getColumns();
        }
        lsql = "SELECT " + columns + " FROM " + s.getTable();
        if (s.getFilter() != null && !s.getFilter().equals("")) {
            lsql = lsql + " WHERE " + s.getFilter();
        }
        lsql = lsql + "; ";
        sql = sql + lsql;

        Statement stmt;
        ResultSet res;
        try {
            stmt = connection.createStatement();
            res = stmt.executeQuery(sql);
            ResultSetMetaData resmeta = res.getMetaData();
            Result result = new Result();
            while (res.next()) {
                Row row = new Row();
                int i = 1;
                boolean bound = true;
                while (bound) {
                    try {
                        row.addColumn(resmeta.getColumnName(i), res.getObject(i));
                    } catch (SQLException e) {
                        bound = false;
                    }
                    i++;
                }
                result.addRow(row);
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return new Result();
        }
    }

    public boolean custom(String sql) {
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return false;
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
}
