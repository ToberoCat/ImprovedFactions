package io.github.toberocat.core.utility.claim;

import io.github.toberocat.core.utility.data.database.DatabaseAccess;
import io.github.toberocat.core.utility.data.database.sql.MySqlDatabase;
import org.jetbrains.annotations.NotNull;

public class DatabaseWorldClaims implements WorldClaims {
    private final MySqlDatabase database;

    public DatabaseWorldClaims() {
        database = DatabaseAccess.accessPipeline(DatabaseAccess.class).database();
    }

    @Override
    public void add(@NotNull Claim claim) {
        database.evalTry("INSERT INTO claims VALUE (%s, %d, %d)",
                claim.getRegistry(),
                claim.getX(),
                claim.getY());
    }

    @Override
    public void remove(@NotNull Claim claim) {
        database.evalTry("DELETE FROM claims WHERE x = %d AND z = %d",
                claim.getX(),
                claim.getY());
    }
}
