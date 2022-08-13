package io.github.toberocat.improvedFactions.handler;

import io.github.toberocat.improvedFactions.database.MySqlDb;
import io.github.toberocat.improvedFactions.exceptions.NoImplementationProvidedException;
import io.github.toberocat.improvedFactions.registry.ImplementationHolder;
import org.jetbrains.annotations.NotNull;

public interface DatabaseHandler {

    static @NotNull DatabaseHandler api() {
        DatabaseHandler implementation = ImplementationHolder.databaseHandler;
        if (implementation == null) throw new NoImplementationProvidedException("database handler");
        return implementation;
    }

    @NotNull MySqlDb getMySql();
}
