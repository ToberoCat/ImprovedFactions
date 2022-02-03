package io.github.toberocat.core.commands.config;

import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.language.LangMessage;
import org.bukkit.entity.Player;

import java.util.List;

public class ConfigSubCommand extends SubCommand {

    public ConfigSubCommand() {
        super("config", LangMessage.COMMAND_CONFIG_DESCRIPTION, true);
        subCommands.add(new ConfigBackupSubCommand());
        subCommands.add(new ConfigSaveSubCommand());
        subCommands.add(new ConfigReloadSubCommand());
        subCommands.add(new ConfigRemoveAllBackupSubCommand());
        subCommands.add(new ConfigConfigureSubCommand());
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {

    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }
}
