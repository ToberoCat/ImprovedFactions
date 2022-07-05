package io.github.toberocat.improvedfactions.commands.factionCommands.extensionCommands;

import io.github.toberocat.improvedfactions.commands.subCommands.SubCommand;
import io.github.toberocat.improvedfactions.extentions.list.ExtensionListLoader;
import io.github.toberocat.improvedfactions.language.Language;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.List;

public class RefreshExtensionSubCommand extends SubCommand {
    public RefreshExtensionSubCommand() {
        super("refresh", "extension.refresh", "Refresh the extension list. This downloads the extension registry");
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        try {
            ExtensionListLoader.RegenerateExtensionList();
            player.sendMessage(Language.getPrefix() + "§fRefreshed the extension registry");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }
}
