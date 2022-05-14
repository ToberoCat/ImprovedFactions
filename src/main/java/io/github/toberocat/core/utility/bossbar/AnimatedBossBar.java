package io.github.toberocat.core.utility.bossbar;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.async.AsyncTask;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class AnimatedBossBar {

    public static final long ANIMATION_TIME = 1;
    public static final int ANIMATION_SPEED = 10000;
    public static final int ANIMATION_MS_FADE = 3000;
    public static final double EPS = 0.001;

    private static final ArrayList<AnimatedBossBar> BOSS_BARS = new ArrayList<>();

    private final BossBar bossBar;
    private final double min, max;
    private int taskId = -1;
    private final Ease ease;
    private Runnable finishCallback;

    public AnimatedBossBar(String title, BarColor color, double min, double max) {
        this(title, color, min, max, time -> time);
    }

    public AnimatedBossBar(String title, BarColor color, double min, double max, Ease ease) {
        this.bossBar = Bukkit.createBossBar(Language.format(title), color, BarStyle.SOLID);
        this.min = min;
        this.max = max;
        this.ease = ease;

        BOSS_BARS.add(this);
    }

    public void fade(Player player, double value) {
        addPlayer(player);
        finishCallback = () -> AsyncTask.runLaterSync(ANIMATION_MS_FADE, () -> removePlayer(player));
        AsyncTask.runLaterSync(ANIMATION_MS_FADE, () -> setValueAnimated(value));
    }


    public static void cleanup() {
        for (AnimatedBossBar bar : BOSS_BARS) bar.bossBar.removeAll();
        BOSS_BARS.clear();
    }

    public void addPlayer(Player player) {
        bossBar.addPlayer(player);
    }

    public void removePlayer(Player player) {
        bossBar.removePlayer(player);
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
        double end = Utility.clamp((value - min) / (max - min), min, max);

        double lastMs = System.currentTimeMillis();

        taskId = new BukkitRunnable() {
            @Override
            public void run() {
                long now = System.currentTimeMillis();
                double elapsed = ease.evaluate((now - lastMs) / ANIMATION_SPEED / ANIMATION_TIME);
                double update = Math.max(bossBar.getProgress() - Utility.lerp(end, start, elapsed), 0);

                bossBar.setProgress(update);

                if ((update - end) < EPS) {
                    if (finishCallback != null) finishCallback.run();
                    cancel();
                }
            }
        }.runTaskTimer(MainIF.getIF(), 0, ANIMATION_TIME).getTaskId();
    }

    private void animateRising(double value) {
        double start = bossBar.getProgress();
        double end = Utility.clamp((value - min) / (max - min), min, max);

        final double lastMs = System.currentTimeMillis();

        taskId = new BukkitRunnable() {
            @Override
            public void run() {
                long now = System.currentTimeMillis();
                double elapsed = ease.evaluate((now - lastMs) / ANIMATION_SPEED / ANIMATION_TIME);
                double update = Math.min(bossBar.getProgress() + Utility.lerp(start, end, elapsed), 1);

                bossBar.setProgress(update);

                if ((end - update) < EPS) {
                    if (finishCallback != null) finishCallback.run();
                    cancel();
                }
            }
        }.runTaskTimer(MainIF.getIF(), 0, ANIMATION_TIME).getTaskId();
    }
}
