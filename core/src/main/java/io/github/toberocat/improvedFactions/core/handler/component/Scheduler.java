package io.github.toberocat.improvedFactions.core.handler.component;

public interface Scheduler {
    int runTimer(Runnable runnable, long intervalTickDelay);
    int runSync(Runnable runnable);
    void cancel(int id);

    void runLater(Runnable runnable, long delay);
}
