package io.github.toberocat.improvedFactions.core.task;

import org.jetbrains.annotations.NotNull;

public class TaskPromise {

    private Runnable runnable;

    public void resolve() {
        synchronized (this) {
            notifyAll();
            if (runnable != null) runnable.run();
        }
    }

    public void await() {
        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void then(@NotNull Runnable runnable) {
        this.runnable = runnable;
    }
}
