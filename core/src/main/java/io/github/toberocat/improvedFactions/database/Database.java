package io.github.toberocat.improvedFactions.database;

import org.jetbrains.annotations.NotNull;

public interface Database {
    void executeUpdate(@NotNull String query, Object... placeholders);
}
