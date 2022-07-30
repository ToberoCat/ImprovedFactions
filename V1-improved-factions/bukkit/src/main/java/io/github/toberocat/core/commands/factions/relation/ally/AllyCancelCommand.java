package io.github.toberocat.core.commands.factions.relation.ally;

import io.github.toberocat.core.utility.command.SubCommand;
import org.bukkit.entity.Player;

import java.util.List;

public class AllyCancelCommand extends SubCommand {
    public AllyCancelCommand() {
        super("ally", descriptionKey, false);
    }

    @Override
    protected void commandExecute(Player player, String[] args) {

    }

    @Override
    protected List<String> commandTab(Player player, String[] args) {
        return null;
    }
}
