package io.github.toberocat.core.utility.async;

public abstract class AsyncRunnable<T> implements Runnable {

    private AsyncTask<T> task;
    private T t;

    public AsyncRunnable() {

    }

    public void instance(AsyncTask<T> task) {
        this.task = task;
    }


    protected void result(T t) {
        this.t = t;
    }

    public T result() {
        return t;
    }

    protected void error(Exception exception) {
        task.onError.accept(exception);
    }
}
