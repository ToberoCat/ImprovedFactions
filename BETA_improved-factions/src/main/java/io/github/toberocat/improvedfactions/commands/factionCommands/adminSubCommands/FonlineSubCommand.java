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

public class FonlineSubCommand extends SubCommand {
    public FonlineSubCommand() {
        super("fonline", "");
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        if (args.length != 1) {
            CommandExecuteError(CommandExecuteError.NotEnoughArgs, player);
            return;
        }

        Faction f = FactionUtils.getFactionByRegistry(args[0]);
        if (f == null) {
            player.sendMessage(Language.getPrefix() + "§cCouldn't find faction to delete");
            return;
        }

        int o = FactionUtils.getPlayersOnline(f).size();
        player.sendMessage(Language.getPrefix() + o + (o == 1 ? " person " : " people ") +
                (o == 1 ? "is" : "are") + " online in §e" + f.getDisplayName());

    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        LinkedList<String> str = new LinkedList<>();

        for (Faction f : Faction.getFACTIONS()) {
            str.add(f.getRegistryName());
        }

        return str;
    }
}
