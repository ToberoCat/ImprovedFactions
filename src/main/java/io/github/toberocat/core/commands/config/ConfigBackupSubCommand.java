package io.github.toberocat.core.commands.config;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.gui.config.backup.ConfigBackupGUI;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.language.LangMessage;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class ConfigBackupSubCommand extends SubCommand  {

    public ConfigBackupSubCommand() {
        super("backup", "config.backup", LangMessage.COMMAND_CONFIG_BACKUP_DESCRIPTION, false);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        new BukkitRunnable() {
            @Override
            public void run() {
                new ConfigBackupGUI(player);
            }
        }.runTask(MainIF.getIF());
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }

    @Override
    protected boolean CommandDisplayCondition(Player player, String[] args) {
        return super.CommandDisplayCondition(player, args) && !MainIF.getIF().getBackupFile().isEmpty();
    }
}
