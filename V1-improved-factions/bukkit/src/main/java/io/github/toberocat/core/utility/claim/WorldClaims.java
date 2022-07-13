package io.github.toberocat.core.utility.claim;

import io.github.toberocat.core.utility.data.database.sql.MySqlDatabase;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class WorldClaims extends ArrayList<Claim> implements MySqlData<WorldClaims> {

    public WorldClaims() {

    }

    @Override
    public boolean save(@NotNull MySqlDatabase sql) {
        return false;
    }

    @Override
    public WorldClaims read(@NotNull MySqlDatabase sql) {
        return null;
    }
}
