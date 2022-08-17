package io.github.toberocat.improvedFactions.core.database.builder;

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

    public Insert setColumns(String... columns) {
        this.columns = String.join(", ", columns);
        return this;
    }

    public String getTable() {
        return table;
    }

    public Insert setTable(String table) {
        this.table = table;
        return this;
    }

    public String[] getData() {
        return data;
    }

    public Insert setData(String... data) {
        this.data = data;
        return this;
    }
}