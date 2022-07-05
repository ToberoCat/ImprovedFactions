package io.github.toberocat.improvedfactions.commands.factionCommands.adminSubCommands;

import io.github.toberocat.improvedfactions.ImprovedFactionsMain;
import io.github.toberocat.improvedfactions.commands.subCommands.SubCommand;
import io.github.toberocat.improvedfactions.language.Language;
import io.github.toberocat.improvedfactions.reports.Report;
import io.github.toberocat.improvedfactions.utility.async.AsyncCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public class ViewReportsSubCommand extends SubCommand {
    public ViewReportsSubCommand() {
        super("viewreports", "");
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        if (args.length != 1) {
            CommandExecuteError(CommandExecuteError.NotEnoughArgs, player);
            return;
        }

        AsyncCore.Run(() -> {
            for (Report report : ImprovedFactionsMain.REPORTS) {
                if (report.getFaction().equals(args[0])) {
                    player.sendMessage(Language.getPrefix() + report.getReason() + " - " + Bukkit.getOfflinePlayer(report.getPlayer()).getName());
                }
            }
            player.sendMessage(Language.getPrefix() + "All reports received for this faction");
        });
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        LinkedList<String> str = new LinkedList<>();

        for (Report report : ImprovedFactionsMain.REPORTS) {
            str.add(report.getFaction());
        }

        return str;
    }
}
