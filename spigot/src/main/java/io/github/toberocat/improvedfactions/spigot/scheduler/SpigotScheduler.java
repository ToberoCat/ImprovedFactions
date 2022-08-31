package io.github.toberocat.improvedfactions.spigot.scheduler;

import io.github.toberocat.improvedFactions.core.handler.component.Scheduler;
import io.github.toberocat.improvedfactions.spigot.MainIF;
import org.bukkit.Bukkit;

public record SpigotScheduler(MainIF plugin) implements Scheduler {

    @Override
    public int runTimer(Runnable runnable, long intervalTickDelay) {
        return Bukkit.getScheduler()
                .runTaskTimer(plugin, runnable, 0, intervalTickDelay)
                .getTaskId();
    }

    @Override
    public int runSync(Runnable runnable) {
        return Bukkit.getScheduler().runTask(plugin, runnable).getTaskId();
    }

    @Override
    public void cancel(int id) {
        Bukkit.getScheduler().cancelTask(id);
    }
}
