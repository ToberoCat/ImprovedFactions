package io.github.toberocat.core.commands.factions.relation;

import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.language.LangMessage;
import org.bukkit.entity.Player;

import java.util.List;

public class RelationSubCommand extends SubCommand {
    public RelationSubCommand() {
        super("relation", LangMessage.COMMAND_RELATION_DESCRIPTION, true);
        subCommands.add(new AllyRelationSubCommand());
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {

    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }
}
