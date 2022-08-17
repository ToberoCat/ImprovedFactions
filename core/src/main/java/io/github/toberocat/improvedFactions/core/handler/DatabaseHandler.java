package io.github.toberocat.improvedFactions.core.handler;

import io.github.toberocat.improvedFactions.core.exceptions.NoImplementationProvidedException;
import io.github.toberocat.improvedFactions.core.registry.ImplementationHolder;
import io.github.toberocat.improvedFactions.core.database.MySqlDatabase;
import org.jetbrains.annotations.NotNull;

public interface DatabaseHandler {

    static @NotNull DatabaseHandler api() {
        DatabaseHandler implementation = ImplementationHolder.databaseHandler;
        if (implementation == null) throw new NoImplementationProvidedException("database handler");
        return implementation;
    }

    @NotNull MySqlDatabase getMySql();
}
