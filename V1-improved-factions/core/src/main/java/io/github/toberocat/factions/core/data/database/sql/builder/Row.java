package io.github.toberocat.factions.core.data.database.sql.builder;

import java.util.HashMap;
import java.util.Set;

public class Row {
    private final HashMap<String, Object> content = new HashMap<>();

    public Row() {
    }

    public Row addColumn(String name, Object content) {
        this.content.put(name, content);
        return this;
    }

    public HashMap<String, Object> getColumns() {
        return content;
    }

    public Object get(String key) {
        return content.get(key);
    }

    public Set<String> getKeys() {
        return content.keySet();
    }
}
