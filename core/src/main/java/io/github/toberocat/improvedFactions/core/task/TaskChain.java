package io.github.toberocat.improvedFactions.core.task;

import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class TaskChain<R> {

    private final AsyncTask<R> rTask;
    private Runnable finished;

    public TaskChain(AsyncTask<R> task, Runnable finished) {
        rTask = task;
        this.finished = finished;
    }

    public static @NotNull <R> TaskChain<R> asyncFirst(@NotNull Supplier<R> action) {
        return new TaskChain<>(new AsyncTask<>(action).start(), null);
    }

    public @NotNull <T> TaskChain<T> async(@NotNull Function<R, T> supplier) {
        AsyncTask<T> tTask = new AsyncTask<>();
        rTask.then(r ->
                tTask.supply(() ->
                        supplier.apply(r)).start());

        return new TaskChain<>(tTask, finished);
    }

    public @NotNull TaskPromise lastAsync(@NotNull Consumer<R> consumer) {
        TaskPromise promise = new TaskPromise();
        rTask.then(r -> {
            consumer.accept(r);
            promise.resolve();
            if (finished != null) finished.run();
        });

        return promise;
    }

    public @NotNull <T> TaskChain<T> sync(@NotNull Function<R, T> supplier) {
        AsyncTask<T> tTask = new AsyncTask<>();
        rTask.then(r ->
                ImprovedFactions.api().getScheduler().runSync(() ->
                        tTask.supply(() ->
                                supplier.apply(r)).start()));

        return new TaskChain<>(tTask, finished);
    }

    public @NotNull TaskPromise lastSync(@NotNull Consumer<R> consumer) {
        TaskPromise promise = new TaskPromise();
        rTask.then(r -> {
            consumer.accept(r);
            promise.resolve();
            if (finished != null) finished.run();
        });

        return promise;
    }

    public @NotNull TaskChain<R> abortIf(@NotNull Predicate<R> abort, @NotNull Consumer<R> aborted) {
        AsyncTask<R> tTask = new AsyncTask<>();
        rTask.then(r -> {
            if (abort.test(r)) aborted.accept(r);
            else tTask.supply(() -> r).start();
        });
        return new TaskChain<>(tTask, finished);
    }

    public @NotNull TaskChain<R> abortIfNull(@NotNull Runnable aborted) {
        AsyncTask<R> tTask = new AsyncTask<>();
        rTask.then(r -> {
            if (r == null) aborted.run();
            else tTask.supply(() -> r).start();
        });
        return new TaskChain<>(tTask, finished);
    }

    public synchronized void then(@NotNull Runnable finished) {
        this.finished = finished;
    }
}
