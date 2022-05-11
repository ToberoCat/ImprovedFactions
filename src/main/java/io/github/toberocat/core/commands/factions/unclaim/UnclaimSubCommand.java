package io.github.toberocat.core.commands.factions.unclaim;

import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import org.bukkit.entity.Player;

import java.util.List;

public class UnclaimSubCommand extends SubCommand {
    public UnclaimSubCommand() {
        super("unclaim", "command.faction.claim.one.description", true);
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
