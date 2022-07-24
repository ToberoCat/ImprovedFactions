package io.github.toberocat.core.commands.extension;

import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

public class ExtensionSubCommand extends SubCommand {
    public ExtensionSubCommand() {
        super("extension", "command.extension.description", true);
        subCommands.addAll(Set.of(
                new ExtensionDownloadSubCommand()
                //new ExtensionRemoveSubCommand()
        ));
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setUseWhenFrozen(true);
    }

    @Override
    protected void commandExecute(Player player, String[] args) {

    }

    @Override
    protected List<String> commandTab(Player player, String[] args) {
        return null;
    }
}
