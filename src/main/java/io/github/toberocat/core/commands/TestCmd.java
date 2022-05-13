package io.github.toberocat.core.commands;

import io.github.toberocat.core.utility.async.AsyncTask;
import io.github.toberocat.core.utility.bossbar.AnimatedBossBar;
import io.github.toberocat.core.utility.bossbar.eases.EaseInOutSine;
import io.github.toberocat.core.utility.command.SubCommand;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.Player;

import java.util.List;

public class TestCmd extends SubCommand {
    public TestCmd() {
        super("test", "", false);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        AnimatedBossBar bar = new AnimatedBossBar("Test bar", BarColor.BLUE, 0, 100, new EaseInOutSine());
        bar.addPlayer(player);
        bar.setValue(100);

        AsyncTask.runLaterSync(0, () -> {
            bar.setValueAnimated(0);
        });
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }
}
