package io.github.toberocat.improvedfactions.commands.factionCommands.adminSubCommands;

import io.github.toberocat.improvedfactions.ImprovedFactionsMain;
import io.github.toberocat.improvedfactions.commands.subCommands.SubCommand;
import io.github.toberocat.improvedfactions.language.Language;
import io.github.toberocat.improvedfactions.reports.Report;
import io.github.toberocat.improvedfactions.utility.async.AsyncCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class RemoveReport extends SubCommand {
    public RemoveReport() {
        super("removeReport", "");
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        if (args.length != 1) {
            CommandExecuteError(CommandExecuteError.NotEnoughArgs, player);
            return;
        }
        ImprovedFactionsMain.REPORTS = ImprovedFactionsMain.REPORTS.stream().filter(x -> !x.getFaction().equals(args[0])).collect(Collectors.toList());

        player.sendMessage(Language.getPrefix() + "All reports removed for this faction");
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
