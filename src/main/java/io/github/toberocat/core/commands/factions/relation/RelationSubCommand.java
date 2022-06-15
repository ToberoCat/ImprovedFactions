package io.github.toberocat.core.commands.factions.relation;

import io.github.toberocat.core.utility.command.SubCommand;
import org.bukkit.entity.Player;

import java.util.List;

public class RelationSubCommand extends SubCommand {
    public RelationSubCommand() {
        super("relation", "command.relation.description", true);
        subCommands.add(new AllyRelationSubCommand());
        subCommands.add(new WarRelationSubCommand());
        subCommands.add(new NeutralRelationSubCommand());
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {

    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }
}
