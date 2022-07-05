package io.github.toberocat.core.utility.claim;

import io.github.toberocat.core.utility.sql.MySql;
import io.github.toberocat.core.utility.sql.MySqlData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class WorldClaims extends ArrayList<Claim> implements MySqlData<WorldClaims> {

    public WorldClaims() {

    }

    @Override
    public boolean save(@NotNull MySql sql) {
        return false;
    }

    @Override
    public WorldClaims read(@NotNull MySql sql) {
        return null;
    }
}
