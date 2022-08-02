package io.github.toberocat.core.utility.data.access;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.data.database.DatabaseAccess;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public abstract class AbstractAccess<A extends AbstractAccess<A>>  implements AccessPipeline<A> {
    private static AbstractAccess<?> currentInstance;

    protected final boolean accessible;

    public AbstractAccess() {
        this.accessible = register();
        currentInstance = this;
    }

    public static <C extends AbstractAccess<C>> boolean isAccess(@NotNull Class<C> accessor) {
        return currentInstance != null && currentInstance.getClass().isAssignableFrom(accessor);
    }

    public static AbstractAccess<?> accessPipeline() {
        if (currentInstance == null) return AccessPipeline.empty();
        return currentInstance.createPipeline();
    }
    public static <C extends AbstractAccess<C>> C accessPipeline(@NotNull Class<C> onlyIf) {
        if (currentInstance == null) return onlyIf.cast(AccessPipeline.empty());
        if (!currentInstance.getClass().isAssignableFrom(onlyIf)) return onlyIf.cast(AccessPipeline.empty());

        return onlyIf.cast(currentInstance.createPipeline());
    }

    public static boolean registerAccessType() {
        if (Boolean.TRUE.equals(MainIF.config().getBoolean("sql.useSql", false)))
            return new DatabaseAccess().accessible;
        return new FileAccess().accessible;
    }

    public static void disposeCurrent() {
        if (currentInstance == null) return;
        currentInstance.dispose();
    }


    public abstract boolean register();

    protected AbstractAccess<A> createPipeline() {
        return this;
    }

    public boolean isUnusable() {
        return !accessible;
    }

    protected <T> T sendProblem(T value, @NotNull String message, Object... placeholders) {
        MainIF.logMessage(Level.WARNING, String.format(message, placeholders));
        return value;
    }

    public void dispose() {
    }

}
