package io.github.toberocat.improvedFactions.core.claims.worldclaim.database.mysql;

import io.github.toberocat.improvedFactions.core.claims.component.Claim;
import io.github.toberocat.improvedFactions.core.claims.worldclaim.WorldClaim;
import io.github.toberocat.improvedFactions.core.database.DatabaseHandle;
import io.github.toberocat.improvedFactions.core.database.mysql.MySqlDatabase;
import io.github.toberocat.improvedFactions.core.database.mysql.builder.Select;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class MySqlWorldClaim implements WorldClaim {

    private final MySqlDatabase database;
    private final String worldName;

    public MySqlWorldClaim(@NotNull String worldName) {
        database = DatabaseHandle.requestMySql();
        this.worldName = worldName;
    }

    @Override
    public void add(@NotNull String registry, int x, int z) {
        database.executeUpdate("INSERT INTO claims VALUE (%s, %s, %d, %d)",
                worldName, registry, x, z);
    }

    @Override
    public void remove(int x, int z) {
        database.executeUpdate("DELETE FROM claims WHERE world = %s, x = %d AND z = %d",
                worldName, x, z);
    }

    @Override
    public @NotNull Stream<Claim> getAllClaims() {
        return database.rowSelect(new Select()
                        .setTable("claims")
                        .setColumns("registry", "x", "z")
                        .setFilter("world = %s", worldName))
                .getRows()
                .stream()
                .map(row -> {
                    int x = Integer.parseInt(row.get("x").toString());
                    int z = Integer.parseInt(row.get("z").toString());

                    return new Claim(worldName, x, z, c -> row.get("registry").toString());
                });
    }

    @Override
    public @Nullable String getRegistry(int x, int z) {
        return database.rowSelect(new Select()
                        .setTable("claims")
                        .setColumns("registry")
                        .setFilter("world = %s AND x = %d AND z = %d",
                                worldName, x, z))
                .readRow(String.class, "registry")
                .orElse(null);
    }
}
