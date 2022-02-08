package io.github.toberocat.core.commands.config;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.gui.config.configure.ConfigConfigureGUI;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.language.LangMessage;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.LinkedList;
import java.util.List;

public class ConfigConfigureSubCommand extends SubCommand {
    public ConfigConfigureSubCommand() {
        super("configure", "config.configure", LangMessage.COMMAND_CONFIG_CONFIGURE_DESCRIPTION, false);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setArgLength(1);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        new BukkitRunnable() {
            @Override
            public void run() {
                new ConfigConfigureGUI(player, args[0]);
            }
        }.runTask(MainIF.getIF());
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return new LinkedList<>(MainIF.getIF().getDataManagers().keySet());
    }
}
