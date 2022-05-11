package io.github.toberocat.core.commands.config;

import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.language.LangMessage;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

public class ConfigSubCommand extends SubCommand {

    public ConfigSubCommand() {
        super("config", LangMessage.COMMAND_CONFIG_DESCRIPTION, true);
        subCommands.addAll(Set.of(
                new ConfigBackupSubCommand(),
                new ConfigSaveSubCommand(),
                new ConfigReloadSubCommand(),
                new ConfigRemoveAllBackupSubCommand(),
                new ConfigConfigureSubCommand()));
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {

    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }
}
