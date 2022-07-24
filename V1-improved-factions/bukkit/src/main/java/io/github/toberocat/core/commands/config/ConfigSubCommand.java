package io.github.toberocat.core.commands.config;

import io.github.toberocat.core.utility.command.SubCommand;
import org.bukkit.entity.Player;

import java.util.List;

public class ConfigSubCommand extends SubCommand {

    public ConfigSubCommand() {
        super("config", "command.config.description", true);
        subCommands.add(new ConfigSaveSubCommand());
        subCommands.add(new ConfigReloadSubCommand());
        subCommands.add(new ConfigRemoveAllBackupSubCommand());
    }

    @Override
    protected void commandExecute(Player player, String[] args) {

    }

    @Override
    protected List<String> commandTab(Player player, String[] args) {
        return null;
    }
}
