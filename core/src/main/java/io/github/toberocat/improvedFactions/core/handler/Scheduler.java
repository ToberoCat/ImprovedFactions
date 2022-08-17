package io.github.toberocat.improvedFactions.core.handler;

public interface Scheduler {
    int runTimer(Runnable runnable, long intervalTickDelay);
    void cancel(int id);
}
