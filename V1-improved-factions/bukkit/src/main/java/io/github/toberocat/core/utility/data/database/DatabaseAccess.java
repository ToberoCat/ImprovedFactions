package io.github.toberocat.core.utility.data.database;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.data.Table;
import io.github.toberocat.core.utility.data.access.AbstractAccess;
import io.github.toberocat.core.utility.data.access.AccessPipeline;
import io.github.toberocat.core.utility.data.annotation.DatabaseField;
import io.github.toberocat.core.utility.ReflectUtility;
import io.github.toberocat.core.utility.data.annotation.TableKey;
import io.github.toberocat.core.utility.data.database.sql.MySqlDatabase;
import io.github.toberocat.core.utility.data.database.sql.SqlCode;
import io.github.toberocat.core.utility.data.database.sql.SqlVar;
import io.github.toberocat.core.utility.data.database.sql.builder.Select;
import io.github.toberocat.core.utility.exceptions.DatabaseAccessException;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Stream;

public class DatabaseAccess extends AbstractAccess<DatabaseAccess> {
    private static final String SCHEMA = "improved_factions";
    protected MySqlDatabase database;

    @Override
    public boolean register() {
        String host = MainIF.config().getString("sql.host", "localhost");
        int port = MainIF.config().getInt("sql.port", 3306);
        String user = MainIF.config().getString("sql.user", "root");
        String password = MainIF.config().getString("sql.password", "1234");

        database = new MySqlDatabase(host, user, password, SCHEMA, port);
        if (!database.connect()) return false;

        if (!database.isConnected()) {
            MainIF.getIF().saveShutdown("&cCouldn't connect to database");
            return false;
        }

        SqlCode.execute(database, SqlCode.CREATE_LAYOUT,
                SqlVar.of("max_len", MainIF.config().getInt("faction.maxNameLen")),
                SqlVar.of("max_tag", MainIF.config().getInt("faction.maxTagLen")));

        MainIF.logMessage(Level.INFO, "&aConnection established to &6mysql");
        return true;
    }

    @Override
    public boolean isUnusable() {
        return super.isUnusable() ||
                database == null ||
                !database.isConnected();
    }

    @Override
    public void dispose() {
        database.close();
    }

    @Override
    public @NotNull AccessPipelineResult<Stream<String>, DatabaseAccess> listInTableStream(@NotNull Table table) {
        if (isUnusable()) return sendProblem(new AccessPipelineResult<>(Stream.empty(),
                this), "The database access wasn't able to establish a " +
                "connection while listing files in %s", table);
        return new AccessPipelineResult<>(database.rowSelect(new Select()
                        .setTable(table.getTable()))
                .getRows()
                .stream()
                .map(row -> {
                    TableKey key = table.getDatabaseClass().getAnnotation(TableKey.class);
                    if (key == null) throw new DatabaseAccessException("No key got specified for the class " +
                            table.getDatabaseClass().getName());
                    return row.get(key.key()).toString();
                }), this);
    }

    @Override
    public @NotNull AccessPipelineResult<List<String>, DatabaseAccess> listInTable(@NotNull Table table) {
        return new AccessPipelineResult<>(listInTableStream(table).item().toList(), this);
    }

    @Override
    public @NotNull AccessPipeline<DatabaseAccess> restoreDefault() {
        database.evalTry("DROP DATABASE IF EXISTS %s", SCHEMA)
                .get(PreparedStatement::executeUpdate);
        register();
        return this;
    }

    @Override
    public @NotNull <T> AccessPipelineResult<T, DatabaseAccess> read(@NotNull Table table, @NotNull String sql) {
        return new AccessPipelineResult<>(null, this);
    }

    @Override
    public @NotNull <T> AccessPipeline<DatabaseAccess> write(@NotNull Table table, T instance) {
        Set<Field> fields =  ReflectUtility.findFields(instance.getClass(), DatabaseField.class);
        fields.forEach(field -> {
            field.
        });
        return this;
    }

    @Override
    public @NotNull AccessPipeline<DatabaseAccess> delete(@NotNull Table table, @NotNull String byKey) {
        return this;
    }

    @Override
    public @NotNull AccessPipelineResult<Boolean, DatabaseAccess> has(@NotNull Table table, @NotNull String byKey) {
        return new AccessPipelineResult<>(false, this);
    }

    public @NotNull AccessPipelineResult<MySqlDatabase, DatabaseAccess> database() {
        return new AccessPipelineResult<>(database, this);
    }

    @Override
    protected DatabaseAccess createPipeline() {
        return this;
    }
}
