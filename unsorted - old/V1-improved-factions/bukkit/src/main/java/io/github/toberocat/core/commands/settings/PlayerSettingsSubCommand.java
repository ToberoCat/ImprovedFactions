package io.github.toberocat.core.commands.settings;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.gui.player.PlayerSettingsGui;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class PlayerSettingsSubCommand extends SubCommand {
    public PlayerSettingsSubCommand() {
        super("user", "user-settings", "command.settings.player.description", false);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setUseWhenFrozen(true);
    }

    @Override
    protected void commandExecute(Player player, String[] args) {
        new BukkitRunnable() {
            @Override
            public void run() {
                new PlayerSettingsGui(player);
            }
        }.runTask(MainIF.getIF());
    }

    @Override
    protected List<String> commandTab(Player player, String[] args) {
        return null;
    }
}
