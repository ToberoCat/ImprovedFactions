package io.github.toberocat.core.commands.factions.claim;

import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.language.LangMessage;
import org.bukkit.entity.Player;

import java.util.List;

public class ClaimSubCommand extends SubCommand {
    public ClaimSubCommand() {
        super("claim", LangMessage.COMMAND_FACTION_CLAIM_ONE_DESCRIPTION, true);
        subCommands.add(new ClaimOneSubCommand());
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setNeedsFaction(SubCommandSettings.NYI.Yes);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {

    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }
}
