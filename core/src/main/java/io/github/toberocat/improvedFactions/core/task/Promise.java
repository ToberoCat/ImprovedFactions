package io.github.toberocat.improvedFactions.core.task;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class Promise<T> implements Consumer<T> {
    private T result;

    public Promise(Consumer<Consumer<T>> callback) {
        synchronized (this) {
            try {
                new Thread(() -> callback.accept(this)).start();
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                accept(null);
            }
        }
    }

    public T result() {
        return result;
    }

    @Override
    public void accept(T t) {
        this.result = t;
        synchronized (this) {
            notifyAll();
        }
    }
}
