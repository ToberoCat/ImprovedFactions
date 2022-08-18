package io.github.toberocat.improvedFactions.core.handler.component;

public interface Scheduler {
    int runTimer(Runnable runnable, long intervalTickDelay);
    void cancel(int id);
}
