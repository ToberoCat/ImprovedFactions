package io.github.toberocat.improvedFactions.database;

import org.jetbrains.annotations.NotNull;

public interface Database {
    boolean executeUpdate(@NotNull String query, Object... placeholders);
    boolean executeUpdate(@NotNull String query, DatabaseVar... placeholders);

}
