package io.github.toberocat.improvedfactions.commands.factionCommands;

import io.github.toberocat.improvedfactions.commands.subCommands.SubCommand;
import io.github.toberocat.improvedfactions.language.LangMessage;
import org.bukkit.entity.Player;

import java.util.List;

public class HelpSubCommand extends SubCommand {
    public HelpSubCommand() {
        super("help", LangMessage.HELP_DESCRIPTION);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {

    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }
}
