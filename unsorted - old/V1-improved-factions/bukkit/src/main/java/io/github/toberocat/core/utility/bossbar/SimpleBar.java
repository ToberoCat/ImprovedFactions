package io.github.toberocat.core.utility.bossbar;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.ArrayList;

import static io.github.toberocat.core.utility.Utility.clamp;
import static io.github.toberocat.core.utility.language.Language.format;


public class SimpleBar {
    private static final ArrayList<SimpleBar> BOSS_BARS = new ArrayList<>();
    protected final BossBar bossBar;
    protected double min, max;

    public SimpleBar(String title, BarColor color, double min, double max) {
        this.bossBar = Bukkit.createBossBar(format(title), color, BarStyle.SOLID);
        this.min = min;
        this.max = max;

        BOSS_BARS.add(this);
    }

    public void dispose() {
        bossBar.removeAll();
        BOSS_BARS.remove(this);
    }

    public static void cleanup() {
        for (SimpleBar bar : BOSS_BARS) bar.bossBar.removeAll();
        BOSS_BARS.clear();
    }

    public void setValue(double value) {
        bossBar.setProgress(clamp((value - min) / (max - min), 0, 1));
    }


    public void addPlayer(Player player) {
        bossBar.addPlayer(player);
    }

    public void removePlayer(Player player) {
        bossBar.removePlayer(player);
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public void setTitle(String title) {
        bossBar.setTitle(format(title));
    }
}
