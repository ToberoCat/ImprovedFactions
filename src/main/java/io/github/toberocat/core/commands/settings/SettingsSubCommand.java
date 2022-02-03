package io.github.toberocat.core.commands.settings;

import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.language.LangMessage;
import org.bukkit.entity.Player;

import java.util.List;

public class SettingsSubCommand extends SubCommand {
    public SettingsSubCommand() {
        super("settings", LangMessage.COMMAND_SETTINGS_DESCRIPTION, true);
        subCommands.add(new PlayerSettingsSubCommand());
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {

    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }
}
