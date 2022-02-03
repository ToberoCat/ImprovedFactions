package io.github.toberocat.core.utility.async;

import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.callbacks.Callback;
import io.github.toberocat.core.utility.callbacks.ResultCallback;
import io.github.toberocat.core.utility.callbacks.ReturnCallback;
import io.github.toberocat.core.utility.exceptions.AlreadyRunException;

/**
 * Run tasks using the threading settings in config.yml performance section
 */
public class AsyncCore<T> {

    private static int tasks = 0;

    public static boolean allTaskFinished() {
        return tasks == 0;
    }

    /**
     * Run tasks using the threading settings in config.yml performance section
     */
    public static <T> AsyncCore<T> Run(ReturnCallback<T> callback) {
        AsyncCore<T> core = new AsyncCore<>(callback);
        core.thread.start();
        return core;
    }

    public static <T> AsyncCore<T> Run(Callback callback) {
        AsyncCore<T> core = new AsyncCore<>(() -> {
            callback.callback();
            return null;
        });
        core.thread.start();
        return core;
    }

    protected ReturnCallback<T> callback;
    protected ResultCallback<T> finishCallback;
    protected Thread thread;
    protected boolean hasFinished;
    protected T threadResult;

    protected AsyncCore(ReturnCallback<T> callback) {
        this.callback = callback;
        this.finishCallback = null;
        this.thread = runThread();
        this.hasFinished = false;
        this.threadResult = null;
    }

    /**
     * Start the thread
     */
    public AsyncCore<T> start() {
        thread.start();
        return this;
    }

    /**
     * Pauses the thread it's called in until the task is done
     * @return the callback
     */
    public AsyncCore<T> await() {
        try {
            thread.join();
            return this;
        } catch (InterruptedException e) {
            Utility.except(e);
            return this;
        }
    }

    /**
     * This returns the value the finished thread returned.
     * This is recommended to use when you await the finish of the thread. Else add a finish callback. @see {@link #setFinishCallback(ResultCallback)}
     * @return The type set while creating
     */
    public T getResult() {
        return threadResult;
    }

    /**
     * This will give you if the thread has already finished
     * @return if thread has finished
     */
    public boolean hasFinished() {
        return hasFinished;
    }

    /**
     * This callback will be fired when the thread finished.
     * When thread is already finished, the callback is run instanly
     */
    public AsyncCore<T> setFinishCallback(ResultCallback<T> finishCallback) {
        this.finishCallback = finishCallback;
        if (hasFinished()) { finishCallback.call(threadResult); }
        return this;
    }

    protected Thread runThread() {
        return new Thread(() -> {
            if (hasFinished) {
                throw new AlreadyRunException("The runnable got called twice. Please make sure that you didn't called it manually");
            }
            addTask();
            threadResult = callback.Callback();
            hasFinished = true;
            if (finishCallback != null) finishCallback.call(threadResult);
            removeTask();
        });
    }

    public static synchronized void addTask() {
        tasks++;
    }

    public static synchronized void removeTask() {
        tasks--;
    }
}
