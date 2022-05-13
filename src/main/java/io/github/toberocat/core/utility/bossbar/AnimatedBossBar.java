package io.github.toberocat.core.utility.bossbar;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.Utility;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class AnimatedBossBar {

    public static final long ANIMATION_TIME = 1;
    public static final int ANIMATION_SPEED = 10000;
    public static final double EPS = 0.001;
    private final BossBar bossBar;
    private final double min, max;
    private int taskId = -1;
    private Ease ease;

    public AnimatedBossBar(String title, BarColor color, double min, double max) {
        this.bossBar = Bukkit.createBossBar(title, color, BarStyle.SOLID);
        this.min = min;
        this.max = max;
        this.ease = time -> time;
    }

    public AnimatedBossBar(String title, BarColor color, double min, double max, Ease ease) {
        this.bossBar = Bukkit.createBossBar(title, color, BarStyle.SOLID);
        this.min = min;
        this.max = max;
        this.ease = ease;
    }

    public void addPlayer(Player player) {
        bossBar.addPlayer(player);
    }

    public Ease getEase() {
        return ease;
    }

    public void setValue(double value) {
        bossBar.setProgress((value - min) / (max - min));
    }

    public void setValueAnimated(double value) {
        Bukkit.getScheduler().cancelTask(taskId);

        if (bossBar.getProgress() < (value - min) / (max - min)) animateRising(value);
        else animateDropping(value);
    }

    private void animateDropping(double value) {
        double start = bossBar.getProgress();
        double end = (value - min) / (max - min);

        double lastMs = System.currentTimeMillis();

        taskId = new BukkitRunnable() {
            @Override
            public void run() {
                long now = System.currentTimeMillis();
                double elapsed = ease.evaluate((now - lastMs) / ANIMATION_SPEED);
                double update = Math.max(bossBar.getProgress() - Utility.lerp(end, start, elapsed / ANIMATION_TIME), 0);

                bossBar.setProgress(update);

                if ((update - end) < EPS) cancel();
            }
        }.runTaskTimer(MainIF.getIF(), 0, ANIMATION_TIME).getTaskId();
    }

    private void animateRising(double value) {
        double start = bossBar.getProgress();
        double end = (value - min) / (max - min);

        final double lastMs = System.currentTimeMillis();

        taskId = new BukkitRunnable() {
            @Override
            public void run() {
                long now = System.currentTimeMillis();
                double elapsed = ease.evaluate((now - lastMs) / ANIMATION_SPEED);
                double update = Math.min(bossBar.getProgress() + Utility.lerp(start, end, elapsed / ANIMATION_TIME), 1);

                bossBar.setProgress(update);

                if ((end - update) < EPS) cancel();
            }
        }.runTaskTimer(MainIF.getIF(), 0, ANIMATION_TIME).getTaskId();
    }
}
