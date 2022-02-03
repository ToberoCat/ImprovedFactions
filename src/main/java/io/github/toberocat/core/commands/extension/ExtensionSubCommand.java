package io.github.toberocat.core.commands.extension;

import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.language.LangMessage;
import org.bukkit.entity.Player;

import java.util.List;

public class ExtensionSubCommand extends SubCommand {
    public ExtensionSubCommand() {
        super("extension", "", true);
        subCommands.add(new ExtensionDownloadSubCommand());
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {

    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }
}
