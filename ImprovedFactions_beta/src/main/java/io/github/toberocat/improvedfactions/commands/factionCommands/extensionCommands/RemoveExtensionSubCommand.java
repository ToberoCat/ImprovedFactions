package io.github.toberocat.improvedfactions.commands.factionCommands.extensionCommands;

import io.github.toberocat.improvedfactions.ImprovedFactionsMain;
import io.github.toberocat.improvedfactions.commands.subCommands.SubCommand;
import io.github.toberocat.improvedfactions.language.Language;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RemoveExtensionSubCommand extends SubCommand {
    public RemoveExtensionSubCommand() {
        super("remove", "extension.remove", "Remove a installed extension. &6&lWarning.&r This deletes the extension");
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        if (args.length == 1) {
            File file = new File(ImprovedFactionsMain.getPlugin().getDataFolder().getPath() + "/Extensions");
            boolean removed = false;
            for (File ext : file.listFiles()) {
                if (ext.isFile() && ext.getName().equals(args[0])) {
                    if (ext.delete()) {
                        player.sendMessage(Language.getPrefix() + Language.format("&fRemoved " + args[0] + ". If there is something buggy, please reload the server"));
                        ImprovedFactionsMain.RemoveExtension(ext.getName().replace(".jar", ""));
                    } else {
                        player.sendMessage(Language.getPrefix() + Language.format("&cCannot delete " + args[0]));
                    }
                    removed = true;
                    break;
                }
            }
            if (!removed) {
                player.sendMessage(Language.getPrefix() + Language.format("&cCouldn't find " + args[0]));
            }
        } else {
            CommandExecuteError(CommandExecuteError.NotEnoughArgs, player);
        }
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        List<String> arguments = new ArrayList<>();
        File file = new File(ImprovedFactionsMain.getPlugin().getDataFolder().getPath() + "/Extensions");
        boolean removed = false;
        for (File ext : file.listFiles()) {
            arguments.add(ext.getName());
        }
        return arguments;
    }
}
