package io.github.toberocat.core.utility.data.access;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.data.Table;
import io.github.toberocat.core.utility.data.database.DatabaseAccess;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.logging.Level;
import java.util.stream.Stream;

public abstract class AbstractAccess {
    private static AbstractAccess currentInstance;

    protected final boolean accessible;

    public AbstractAccess() {
        this.accessible = register();
        currentInstance = this;
    }

    public static AccessPipeline<?> accessPipeline(@NotNull Class<? extends AbstractAccess> onlyIf) {
        if (currentInstance == null) return AccessPipeline.empty();
        if (!currentInstance.getClass().isAssignableFrom(onlyIf)) return AccessPipeline.empty();

        return currentInstance.createPipeline();
    }

    public static boolean registerAccessType() {
        if (Boolean.TRUE.equals(MainIF.config().getBoolean("sql.useSql", false))) return new DatabaseAccess().accessible;
        return new FileAccess().accessible;
    }

    public static void disposeCurrent() {
        if (currentInstance == null) return;
        currentInstance.dispose();
    }


    public abstract boolean register();

    public abstract Stream<String> listInTableStream(@NotNull Table table);

    public abstract List<String> listInTable(@NotNull Table table);

    public abstract void restoreDefault();

    public abstract <T> T read(@NotNull Table table, @NotNull String byKey, @NotNull Class<T> clazz);

    public abstract <T> void write(@NotNull Table table, T instance);

    public abstract void delete(@NotNull Table table, @NotNull String byKey);

    public abstract void has(@NotNull Table table, @NotNull String byKey);

    protected abstract AccessPipeline<?> createPipeline();

    public boolean isUnusable() {
        return !accessible;
    }

    protected  <T> T sendProblem(T value, @NotNull String message, Object... placeholders) {
        MainIF.logMessage(Level.WARNING, String.format(message, placeholders));
        return value;
    }
    public void dispose() {
    }

}
