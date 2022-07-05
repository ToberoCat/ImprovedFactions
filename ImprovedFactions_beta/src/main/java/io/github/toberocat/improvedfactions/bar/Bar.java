package io.github.toberocat.improvedfactions.bar;

import io.github.toberocat.improvedfactions.ImprovedFactionsMain;
import io.github.toberocat.improvedfactions.language.Language;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.LinkedList;

public class Bar {

    private static final LinkedList<Bar> BARS = new LinkedList<>();

    private int taskID = -1;

    private final ImprovedFactionsMain plugin;
    private BossBar bar;

    private int currentValue;
    private int maxValue;

    public Bar(ImprovedFactionsMain plugin, int maxValue) {
        this.plugin = plugin;
        this.maxValue = maxValue;
        this.currentValue = maxValue;

        BARS.add(this);
    }

    public static void Disable() {
        for (Bar bar : BARS) {
            bar.bar.removeAll();
            if (Bukkit.getScheduler().isCurrentlyRunning(bar.taskID)) {
                Bukkit.getScheduler().cancelTask(bar.taskID);
            }
        }
    }

    public void addPlayer(Player player) {
        bar.addPlayer(player);
    }

    public void setCurrentValue(int value) {
        playAnimation(value);
    }

    private void playAnimation(final int goingToValue) {
        if (Bukkit.getScheduler().isCurrentlyRunning(taskID)) {
            Bukkit.getScheduler().cancelTask(taskID);
        }
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {

            final float barMaxValue = (float)currentValue/maxValue;
            final float wantedValue = (float)goingToValue / maxValue;
            float progress = barMaxValue;
            @Override
            public void run() {
                System.out.println(wantedValue);
                System.out.println(barMaxValue);
                progress -= lerp(barMaxValue, wantedValue, 1f/20f);

                bar.setProgress(progress);

                if (progress <= Math.abs(barMaxValue - wantedValue)) {
                    Bukkit.getScheduler().cancelTask(taskID);
                    System.out.println("Cancel");
                }
            }
        }, 0, 20L);
        currentValue = goingToValue;
    }

    private float lerp(float point1, float point2, float alpha) {
        return point1 + alpha * (point2 - point1);
    }


    public void createBar(String title, BarColor color) {
        bar = Bukkit.createBossBar(Language.format(title), color, BarStyle.SOLID);
        bar.setVisible(true);
    }

    public BossBar getBar() {
        return bar;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }
}
