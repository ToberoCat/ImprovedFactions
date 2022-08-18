package io.github.toberocat.improvedfactions.spigot.scheduler;

import io.github.toberocat.improvedFactions.core.handler.component.Scheduler;
import io.github.toberocat.improvedfactions.spigot.MainIF;
import org.bukkit.Bukkit;

public class SpigotScheduler implements Scheduler {

    private final MainIF plugin;

    public SpigotScheduler(MainIF plugin) {
        this.plugin = plugin;
    }

    @Override
    public int runTimer(Runnable runnable, long intervalTickDelay) {
        return Bukkit.getScheduler()
                .runTaskTimer(plugin, runnable, 0, intervalTickDelay)
                .getTaskId();
    }

    @Override
    public void cancel(int id) {
        Bukkit.getScheduler().cancelTask(id);
    }
}
