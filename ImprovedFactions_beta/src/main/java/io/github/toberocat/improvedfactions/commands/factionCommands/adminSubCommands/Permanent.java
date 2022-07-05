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

public class Permanent extends SubCommand {
    public Permanent() {
        super("permanent", "");
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        if (args.length != 1) {
            CommandExecuteError(CommandExecuteError.NotEnoughArgs, player);
            return;
        }

        Faction f = FactionUtils.getFactionByRegistry(args[0]);
        if (f == null) {
            player.sendMessage(Language.getPrefix() + "§cCouldn't find faction to make permanent");
            return;
        }

        f.setPermanent(!f.isPermanent());
        player.sendMessage(Language.getPrefix() + "§fFaction " + (f.isPermanent() ? "is now permanent" : "isn't permanent any more"));
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
