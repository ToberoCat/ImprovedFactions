package io.github.toberocat.improvedfactions.commands.factionCommands;

import io.github.toberocat.improvedfactions.commands.factionCommands.extensionCommands.*;
import io.github.toberocat.improvedfactions.commands.subCommands.SubCommand;
import io.github.toberocat.improvedfactions.language.LangMessage;
import io.github.toberocat.improvedfactions.language.Language;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ExtensionSubCommand extends SubCommand {
    public static List<SubCommand> subCommands = new ArrayList<>();

    public ExtensionSubCommand() {
        super("extension", "Use the extensions to add features easily");
        subCommands.add(new DownloadExtensionSubCommand());
        subCommands.add(new RefreshExtensionSubCommand());
        subCommands.add(new ListExtensionSubCommand());
        subCommands.add(new RemoveExtensionSubCommand());
        subCommands.add(new UpgradeExtensionsSubCommand());
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        if(!SubCommand.CallSubCommands(subCommands, player, args)) {
            Language.sendMessage(LangMessage.THIS_COMMAND_DOES_NOT_EXIST, player);
        }
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return SubCommand.CallSubCommandsTab(subCommands, player, args);
    }
}
