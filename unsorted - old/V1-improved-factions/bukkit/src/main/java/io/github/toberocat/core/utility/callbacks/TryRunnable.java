package io.github.toberocat.core.utility.callbacks;

import io.github.toberocat.core.utility.async.AsyncTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public abstract class TryRunnable<T> implements Runnable {
    private Consumer<Exception> c = Throwable::printStackTrace;
    private T result = null;

    protected abstract T execute() throws Exception;

    @Override
    public void run() {
        try {
            result = execute();
        } catch (Exception e) {
            c.accept(e);
        }
    }

    public @Nullable T get() {
        run();
        return result;
    }

    public TryRunnable<T> get(ExceptConsumer<T> r) {
        AsyncTask.runLater(0, () -> {
            run();
            try {
                r.run(result);
            } catch (Exception e) {
                c.accept(e);
            }
        });

        return this;
    }

    public TryRunnable<T> except(@NotNull Consumer<Exception> consumer) {
        c = consumer;
        return this;
    }
}
