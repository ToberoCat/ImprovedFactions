package io.github.toberocat.improvedFactions.database.builder;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Result {
    private final LinkedList<Row> rows = new LinkedList<>();

    public Result() {
    }

    public Optional<Boolean> hasItem() {
        return Optional.of(rows.size() > 0);
    }

    public <T> Optional<T> readRow(@NotNull Class<T> clazz, @NotNull String column) {
        if (rows.size() == 0) return Optional.empty();
        Row row = rows.get(0);

        if (!row.getColumns().containsKey(column)) return Optional.empty();
        return Optional.of(clazz.cast(row.get(column)));
    }

    public void addRow(Row row) {
        rows.add(row);
    }

    public List<Row> getRows() {
        return rows;
    }
}
