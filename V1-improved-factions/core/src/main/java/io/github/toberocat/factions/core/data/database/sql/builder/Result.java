package io.github.toberocat.factions.core.data.database.sql.builder;

import java.util.LinkedList;
import java.util.List;

public class Result {
    private final LinkedList<Row> rows = new LinkedList<>();

    public Result() {
    }

    public void addRow(Row row) {
        rows.add(row);
    }

    public List<Row> getRows() {
        return rows;
    }
}
