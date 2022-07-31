package io.github.toberocat.core.commands.factions.relation.war;

import io.github.toberocat.core.utility.command.SubCommand;
import org.bukkit.entity.Player;

import java.util.List;

public class WarSubCommand extends SubCommand {
    public WarSubCommand() {
        super("war", "command.war.description", true);
        subCommands.addAll(List.of(
                new DeclareWarCommand(),
                new SurrenderWarCommand()
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
