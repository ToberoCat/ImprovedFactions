package io.github.toberocat.core.utility.async;


import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.callbacks.ResultCallback;
import io.github.toberocat.core.utility.callbacks.ReturnCallback;
import io.github.toberocat.core.utility.exceptions.AlreadyRunException;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.stream.Stream;

/**
 * It allows you to run code on a separate thread
 */
public class AsyncTask<T> {

    protected final static HashMap<Integer, AsyncTask> TASKS = new HashMap<>();
    protected final AsyncRunnable<T> callback;
    protected final Thread thread;
    protected int id;

    protected ResultCallback<T> onFinish;
    protected Consumer<Exception> onError;

    protected boolean hasFinished;
    protected T threadResult;

    private AsyncTask(AsyncRunnable<T> callback) {
        this.callback = callback;
        this.onFinish = null;
        this.thread = runThread();
        this.hasFinished = false;
        this.threadResult = null;
    }

    public static void cleanup() {
        MainIF.getPlugin(MainIF.class).getLogger().log(Level.INFO, "Waiting for threads to finish");
        waitForAllThreads();
        MainIF.getPlugin(MainIF.class).getLogger().log(Level.INFO, "Continued cleaning");
        TASKS.clear();
    }

    public static void collect(List<Integer> tasks, Runnable callback) {
        AtomicInteger alreadyFinished = new AtomicInteger();
        TASKS.entrySet().stream().filter(x -> tasks.contains(x.getKey())).forEach(x -> x.getValue().then(() -> {
            System.out.println("Finished one thread");
            alreadyFinished.getAndIncrement();
            if (alreadyFinished.intValue() >= tasks.size()) callback.run();
        }));
    }

    public static void runSync(Runnable runnable) {
        runLaterSync(0, runnable);
    }



    public static void callEventSync(Event event) {
        runSync(() -> Bukkit.getPluginManager().callEvent(event));
    }

    public static <T> T find(T[] array, Predicate<T> predicate) {
        return Stream.of(array).parallel().filter(predicate).findFirst().orElse(null);
    }

    public static <T> T find(Collection<T> list, Predicate<T> predicate) {
        return list.parallelStream().filter(predicate).findFirst().orElse(null);
    }

    public static void waitForAllThreads() {
        if (TASKS.size() <= 0) return;
        synchronized (TASKS) {

            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            ScheduledFuture<?> scheduledFuture = executor.scheduleAtFixedRate(() -> {
                if (TASKS.size() <= 0) TASKS.notifyAll();
            }, 0, 1, TimeUnit.SECONDS);

            try {
                TASKS.wait();
                scheduledFuture.cancel(false);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * It returns true if the number of tasks is zero
     *
     * @return The number of tasks that are currently running.
     */
    public static boolean haveAllTasksFinished() {
        return TASKS.size() == 0;
    }

    /**
     * This function returns the number of tasks that are currently running.
     *
     * @return The number of tasks that are currently running.
     */
    public static synchronized Set<Integer> getRunningTasks() {
        return TASKS.keySet();
    }

    /**
     * Execute a code piece on a separate thread. This methode allows you to return a result.
     * To get it, you could use {@link AsyncTask#getResult()} to get the result (Only if you are sure the thread ahs finished)
     * You could also use {@link AsyncTask#then(ResultCallback)} to get the result when the thread has finished
     */
    public static <T> AsyncTask<T> run(ReturnCallback<T> callback) {
        AsyncTask<T> core = new AsyncTask<>(new AsyncRunnable<>() {
            @Override
            public void run() {
                result(callback.Callback());
            }
        });
        core.thread.start();
        return core;
    }

    public static <T> AsyncTask<T> returnItem(T item) {
        AsyncTask<T> core = new AsyncTask<>(new AsyncRunnable<T>() {
            @Override
            public void run() {
            }
        });
        core.id = addTask(core);
        core.threadResult = item;
        core.hasFinished = true;
        if (core.onFinish != null) core.onFinish.call(core.threadResult);
        removeTask(core.id);

        return core;
    }

    /**
     * Runs a task later.
     *
     * @param milliseconds The amount of time in milliseconds to wait before running the callback.
     * @param callback     The code to run.
     */
    public static void runLater(long milliseconds, Runnable callback) {
        Bukkit.getScheduler().runTaskLater(MainIF.getPlugin(MainIF.class), callback, milliseconds / 1000 * 20L);
    }

    public static void runLaterSync(long milliseconds, Runnable callback) {
        Bukkit.getScheduler().runTaskLater(MainIF.getIF(), callback, milliseconds / 1000 * 20L);
    }

    /**
     * Execute a code piece on a separate thread
     */
    public static <T> AsyncTask<T> run(AsyncRunnable<T> callback) {
        AsyncTask<T> core = new AsyncTask<>(callback);
        core.thread.start();
        return core;
    }

    /**
     * Execute a code piece on a separate thread
     */
    public static <T> AsyncTask<T> run(Runnable callback) {
        AsyncTask<T> core = new AsyncTask<>(new AsyncRunnable<T>() {
            @Override
            public void run() {
                callback.run();
            }
        });
        core.thread.start();
        return core;
    }

    private static synchronized <T> int addTask(AsyncTask<T> task) {
        synchronized (TASKS) {
            int id = TASKS.size();
            TASKS.put(id, task);

            return id;
        }
    }

    private static synchronized void removeTask(int id) {
        synchronized (TASKS) {
            TASKS.remove(id);
            if (TASKS.size() <= 0) TASKS.notify();
        }
    }

    /**
     * Pauses the thread the methode is called in until the task is done
     *
     * @return the callback
     */
    public T await() {
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return getResult();
    }

    /**
     * This returns the value the finished thread returned.
     * This is recommended to use when you await the finish of the thread. Else add a finish callback. @see {@link #then(ResultCallback)}
     *
     * @return The type set while creating
     */
    public T getResult() {
        return threadResult;
    }

    /**
     * This will give you if the thread has already finished
     *
     * @return if thread has finished
     */
    public boolean hasFinished() {
        return hasFinished;
    }

    /**
     * This callback will be fired when the thread finished.
     * When thread is already finished, the callback is run instanly
     */
    public AsyncTask<T> then(ResultCallback<T> onFinish) {
        this.onFinish = onFinish;
        if (hasFinished()) {
            onFinish.call(threadResult);
        }
        return this;
    }

    public AsyncTask<T> then(Runnable onFinish) {
        this.onFinish = (result) -> onFinish.run();
        if (hasFinished()) {
            onFinish.run();
        }
        return this;
    }

    public AsyncTask<T> except(Consumer<Exception> onError) {
        this.onError = onError;

        return this;
    }

    public int getId() {
        return id;
    }

    private Thread runThread() {
        return new Thread(() -> {
            if (hasFinished) {
                throw new AlreadyRunException("The runnable got called twice. Please make sure that you didn't called it manually");
            }
            id = addTask(this);
            try {
                callback.instance(this);
                callback.run();

                threadResult = callback.result();
                hasFinished = true;
                if (onFinish != null) onFinish.call(threadResult);
            } catch (Exception e) {
                Utility.except(e);
            } finally {
                removeTask(id);
            }
        });
    }
}
