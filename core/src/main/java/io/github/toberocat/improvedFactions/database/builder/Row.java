package io.github.toberocat.improvedFactions.database.builder;

import java.util.HashMap;
import java.util.Set;

public class Row {
    private final HashMap<String, Object> content = new HashMap<>();

    public Row() {
    }

    public void addColumn(String name, Object content) {
        this.content.put(name, content);
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
