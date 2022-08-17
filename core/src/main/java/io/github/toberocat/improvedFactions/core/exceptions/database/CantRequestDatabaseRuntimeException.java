package io.github.toberocat.improvedFactions.core.exceptions.database;

import org.jetbrains.annotations.NotNull;

public class CantRequestDatabaseRuntimeException extends RuntimeException {
    public CantRequestDatabaseRuntimeException(@NotNull String db) {
        super("Can't request database " + db + " due to invalid settings /" +
                " missing allowance in config files");
    }
}
