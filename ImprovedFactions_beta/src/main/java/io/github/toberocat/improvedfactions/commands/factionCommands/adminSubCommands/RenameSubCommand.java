package io.github.toberocat.improvedfactions.commands.factionCommands.adminSubCommands;

import io.github.toberocat.improvedfactions.ImprovedFactionsMain;
import io.github.toberocat.improvedfactions.commands.subCommands.SubCommand;
import io.github.toberocat.improvedfactions.factions.Faction;
import io.github.toberocat.improvedfactions.factions.FactionUtils;
import io.github.toberocat.improvedfactions.language.Language;
import io.github.toberocat.improvedfactions.reports.Report;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public class RenameSubCommand extends SubCommand {
    public RenameSubCommand() {
        super("rename", "");
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        if (args.length != 2) {
            CommandExecuteError(CommandExecuteError.NotEnoughArgs, player);
            return;
        }

        Faction f = FactionUtils.getFactionByRegistry(args[0]);
        if (f == null) {
            player.sendMessage(Language.getPrefix() + "§cCouldn't find faction to delete");
            return;
        }

        f.setDisplayName(args[1]);
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        LinkedList<String> str = new LinkedList<>();

        if (args.length <= 1) {
            for (Faction report : Faction.getFACTIONS()) {
                str.add(report.getRegistryName());
            }
        } else {
            str.add("NewName");
        }

        return str;
    }
}
