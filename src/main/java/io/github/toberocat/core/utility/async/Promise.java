package io.github.toberocat.core.utility.async;

import io.github.toberocat.core.utility.callbacks.ResultCallback;

import java.util.function.Consumer;

public class Promise<T> implements ResultCallback<T> {
    private T result;

    public Promise(Consumer<ResultCallback<T>> callback) {
        synchronized (this) {
            try {
                new Thread(() -> callback.accept(this)).start();
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public T result() {
        return result;
    }

    @Override
    public void call(T type) {
        System.out.println(type);
        this.result = type;
        synchronized (this) {
            notifyAll();
        }
    }
}
