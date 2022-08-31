package io.github.toberocat.improvedFactions.core.task;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class SharedTask {
    private static final Map<String, Runnable> tasks = new HashMap<>();
    private static final List<String> runningTasks = new ArrayList<>();

    public static <R> @NotNull TaskChain<R> sharedTask(@NotNull String taskId, @NotNull Supplier<R> supplier) {
        TaskChain<R> chain = TaskChain.asyncFirst(() -> {
            if (runningTasks.contains(taskId)) {
                new Promise<>(resolve -> tasks.put(taskId, () -> resolve.accept(null)));
            }

            runningTasks.add(taskId);
            return supplier.get();
        });
        chain.then(() -> {
            runningTasks.remove(taskId);
            Runnable runnable = tasks.get(taskId);
            if (runnable != null) runnable.run();
        });
        return chain;
    }
}
