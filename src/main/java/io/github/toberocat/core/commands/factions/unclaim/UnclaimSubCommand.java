package io.github.toberocat.core.commands.factions.unclaim;

import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.language.LangMessage;
import org.bukkit.entity.Player;

import java.util.List;

public class UnclaimSubCommand extends SubCommand {
    public UnclaimSubCommand() {
        super("unclaim", LangMessage.COMMAND_FACTION_CLAIM_ONE_DESCRIPTION, true);
        subCommands.add(new UnclaimOneSubCommand());
        subCommands.add(new UnclaimAutoSubCommand());
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setNeedsFaction(SubCommandSettings.NYI.Yes);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        if (args.length == 0) {
            subCommands.get(0).CallSubCommand(player, new String[]{});
        }
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }
}
