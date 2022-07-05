package io.github.toberocat.improvedfactions.commands.factionCommands.adminSubCommands;

import io.github.toberocat.improvedfactions.commands.subCommands.SubCommand;
import io.github.toberocat.improvedfactions.factions.Faction;
import io.github.toberocat.improvedfactions.factions.FactionUtils;
import io.github.toberocat.improvedfactions.language.Language;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public class FrozeAdminCommand extends SubCommand {
    public FrozeAdminCommand() {
        super("freeze", "");
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        if (args.length != 1) {
            CommandExecuteError(CommandExecuteError.NotEnoughArgs, player);
            return;
        }

        Faction f = FactionUtils.getFactionByRegistry(args[0]);
        if (f == null) {
            player.sendMessage(Language.getPrefix() + "§cCouldn't find faction to freeze");
            return;
        }

        f.setFrozen(!f.isFrozen());
        player.sendMessage(Language.getPrefix() + "§fFaction " + (f.isFrozen() ? "is now frozen" : "isn't frozen any more"));
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        LinkedList<String> str = new LinkedList<>();

        if (args.length <= 1) {
            for (Faction report : Faction.getFACTIONS()) {
                str.add(report.getRegistryName());
            }
        }

        return str;
    }
}
