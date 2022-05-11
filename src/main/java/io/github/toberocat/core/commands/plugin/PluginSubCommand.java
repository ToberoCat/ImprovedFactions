package io.github.toberocat.core.commands.plugin;

import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import org.bukkit.entity.Player;

import java.util.List;

public class PluginSubCommand extends SubCommand {
    public PluginSubCommand() {
        super("plugin", "command.plugin.description", true);
        subCommands.add(new PluginDisableSubCommand());
        subCommands.add(new PluginStandbySubCommand());
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setUseWhenFrozen(true);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {

    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }
}
