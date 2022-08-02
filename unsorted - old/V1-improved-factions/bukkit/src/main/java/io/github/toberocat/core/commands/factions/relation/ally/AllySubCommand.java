package io.github.toberocat.core.commands.factions.relation.ally;

import io.github.toberocat.core.utility.command.SubCommand;
import org.bukkit.entity.Player;

import java.util.List;

// ToDo Replace with gui
public class AllySubCommand extends SubCommand {
    public AllySubCommand() {
        super("ally", "command.ally.description", true);
        subCommands.addAll(List.of(
           new AllyInviteCommand(),
           new AllyAcceptCommand(),
           new AllyCancelCommand(),
           new AllyRemoveCommand()
        ));
    }

    @Override
    protected void commandExecute(Player player, String[] args) {

    }

    @Override
    protected List<String> commandTab(Player player, String[] args) {
        return null;
    }
}
