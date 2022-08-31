package io.github.toberocat.improvedFactions.core.task;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class AsyncTask<R> implements Runnable {

    private Supplier<R> supplier;
    private Consumer<R> consumer;
    private R result;
    private boolean run;

    public AsyncTask() {
    }

    public AsyncTask(@NotNull Supplier<R> supplier) {
        this.supplier = supplier;
    }

    public @NotNull AsyncTask<R> start() {
        Thread thread = new Thread(this);
        thread.start();
        return this;
    }

    public @NotNull AsyncTask<R> supply(@NotNull Supplier<R> supplier) {
        this.supplier = supplier;
        return this;
    }

    public void then(@NotNull Consumer<R> then) {
        this.consumer = then;
        if (run) this.consumer.accept(result);
    }

    @Override
    public void run() {
        result = supplier.get();

        if (consumer == null) run = true;
        else consumer.accept(result);
    }
}
