package io.github.toberocat.improvedfactions.commands.factionCommands;

import io.github.toberocat.improvedfactions.ImprovedFactionsMain;
import io.github.toberocat.improvedfactions.commands.subCommands.SubCommand;
import io.github.toberocat.improvedfactions.language.LangMessage;
import io.github.toberocat.improvedfactions.language.Language;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;

public class ReloadConfigSubCommand extends SubCommand {
    public ReloadConfigSubCommand() {
        super("reload", "reload", LangMessage.RELOAD_DESCRIPTION);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        ImprovedFactionsMain.getPlugin().ReloadConfigs();
        Language.init(ImprovedFactionsMain.getPlugin(), new File(ImprovedFactionsMain.getPlugin().getDataFolder().getPath() + "/lang"));

        player.sendMessage(Language.getPrefix() + Language.format("Reloaded the configs"));
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }
}
