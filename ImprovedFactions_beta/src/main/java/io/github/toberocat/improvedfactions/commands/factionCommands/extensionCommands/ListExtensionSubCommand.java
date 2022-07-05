package io.github.toberocat.improvedfactions.commands.factionCommands.extensionCommands;

import io.github.toberocat.improvedfactions.ImprovedFactionsMain;
import io.github.toberocat.improvedfactions.commands.subCommands.SubCommand;
import io.github.toberocat.improvedfactions.extentions.ExtensionContainer;
import io.github.toberocat.improvedfactions.language.Language;
import org.bukkit.entity.Player;

import java.util.List;

public class ListExtensionSubCommand extends SubCommand {
    public ListExtensionSubCommand() {
        super("list", "extension.list", "Lists the installed extensions");
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        for (String key : ImprovedFactionsMain.extensions.keySet()) {
            ExtensionContainer extension = ImprovedFactionsMain.extensions.get(key);
            player.sendMessage(Language.getPrefix() + Language.format((extension.getExtension().isEnabled() ? "&e&l" : "&c&l")
                    + key + "&r - &fv" + extension.getExtension().getRegistry().getVersion() +
                    (!extension.getExtension().getUpdateChecker().isNewestVersion() ? "&a A newer version available" : "")));
        }
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }
}
