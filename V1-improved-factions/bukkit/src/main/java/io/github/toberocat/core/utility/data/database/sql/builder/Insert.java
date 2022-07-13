package io.github.toberocat.core.utility.data.database.sql.builder;

import java.util.List;

public class Insert {
    private String table = "";
    private String columns = "";
    private String[] data = null;

    public Insert(String table, String columns, String... data) {
        this.table = table;
        this.columns = columns;
        this.data = data;
    }

    public Insert() {
    }

    public String getColumns() {
        return columns;
    }

    public String getTable() {
        return table;
    }

    public String[] getData() {
        return data;
    }

    public Insert setTable(String table) {
        this.table = table;
        return this;
    }

    public Insert setColumns(String columns) {
        this.columns = columns;
        return this;
    }

    public Insert setData(String[] data) {
        this.data = data;
        return this;
    }
}