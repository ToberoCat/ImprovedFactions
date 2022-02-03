package io.github.toberocat.core.commands.settings;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.gui.settings.PlayerSettingsGui;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.language.LangMessage;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class PlayerSettingsSubCommand extends SubCommand {
    public PlayerSettingsSubCommand() {
        super("player", LangMessage.COMMAND_SETTINGS_PLAYER_DESCRIPTION, false);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        new BukkitRunnable() {
            @Override
            public void run() {
                new PlayerSettingsGui(player);
            }
        }.runTask(MainIF.getIF());
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }
}
