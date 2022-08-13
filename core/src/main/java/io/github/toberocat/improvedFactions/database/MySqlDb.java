package io.github.toberocat.improvedFactions.database;

import com.mysql.cj.jdbc.MysqlDataSource;
import io.github.toberocat.improvedFactions.database.builder.*;
import io.github.toberocat.improvedFactions.utils.Logger;
import org.jetbrains.annotations.NotNull;

import java.sql.*;

public class MySqlDb implements Database {

    private final String host, user, password, db;
    private final int port;
    private Connection connection;

    public MySqlDb(@NotNull String host,
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
            Logger.api().logException(e);
            return false;
        }
    }

    public boolean isConnected() {
        if (connection == null) return false;

        try {
            return connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
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

        return executeUpdate("INSERT INTO %s (%s) VALUES (%s);", table, columns, sqldata);
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


            sql.append("INSERT INTO ")
                    .append(b.getTable())
                    .append(" (")
                    .append(b.getColumns())
                    .append(") VALUES (")
                    .append(sqldata)
                    .append("); ");

        }

        return executeUpdate(sql.toString());
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

        return executeUpdate("UPDATE %s SET %s WHERE %s;", table, change, filter);
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
            sql.append("UPDATE ")
                    .append(u.getTable())
                    .append(" SET ")
                    .append(change)
                    .append(" WHERE ")
                    .append(u.getFilter())
                    .append("; ");
        }

        return executeUpdate(sql.toString());
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

        return getResult(sql);
    }

    @NotNull
    private Result getResult(String sql) {
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

        return getResult(sql);
    }

    @Override
    public boolean executeUpdate(@NotNull String query, Object... placeholders) {
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            stmt.execute(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (stmt == null) return false;
        try {
            stmt.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
