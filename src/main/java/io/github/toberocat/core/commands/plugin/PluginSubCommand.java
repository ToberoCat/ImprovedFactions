package io.github.toberocat.core.commands.plugin;

import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.language.LangMessage;
import org.bukkit.entity.Player;

import java.util.List;

public class PluginSubCommand extends SubCommand {
    public PluginSubCommand() {
        super("plugin", LangMessage.COMMAND_PLUGIN_DESCRIPTION, true);
        subCommands.add(new PluginDisableSubCommand());
        subCommands.add(new PluginStandbySubCommand());
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {

    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }
}
