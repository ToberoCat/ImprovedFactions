package io.github.toberocat.core.utility.claim.component.database;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.claim.component.Claim;
import io.github.toberocat.core.utility.claim.component.WorldClaims;
import io.github.toberocat.core.utility.data.database.DatabaseAccess;
import io.github.toberocat.core.utility.data.database.sql.MySqlDatabase;
import io.github.toberocat.core.utility.data.database.sql.builder.Select;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.util.stream.Stream;

public class DatabaseWorldClaims extends WorldClaims {

    private static final String QUERY = """
            CREATE TABLE claims_%s
            (
                registry VARCHAR(%d) not null,
                x        int         null,
                z        int         null,
                constraint claims_%s_pk
                    primary key (x, z)
            );
            """;

    private final MySqlDatabase database;
    private final String table;

    public DatabaseWorldClaims(@NotNull String worldName) {
        super(worldName);
        database = DatabaseAccess.accessPipeline(DatabaseAccess.class).database();

        String converted = convertWorldName(worldName);

        database.evalTry(QUERY,
                        converted,
                        MainIF.config().getInt("faction.maxNameLen", 10),
                        converted)
                .get(PreparedStatement::executeUpdate);

        table = "claims_" + converted;
    }

    public static DatabaseWorldClaims loadWorldClaim(@NotNull String worldName) {

    }

    @Override
    public void add(@NotNull Claim claim) {
        database.evalTry("INSERT INTO %s VALUE (%s, %d, %d)",
                        table,
                        claim.getRegistry(),
                        claim.getX(),
                        claim.getY())
                .get(PreparedStatement::executeUpdate);
    }

    @Override
    public void remove(@NotNull Claim claim) {
        database.evalTry("DELETE FROM %s WHERE x = %d AND z = %d",
                        table,
                        claim.getX(),
                        claim.getY())
                .get(PreparedStatement::executeUpdate);
    }

    @Override
    public Stream<Claim> getClaims() {
        return database.rowSelect(new Select()
                        .setTable(table)
                        .setColumns("x", "z", "registry"))
                .getRows()
                .stream()
                .map(row -> new Claim((int) row.get("x"), (int) row.get("z"), row.get("registry").toString()));
    }

    @Override
    public void save() {
        // Database already stores everything, so no need to save it again
    }
}
