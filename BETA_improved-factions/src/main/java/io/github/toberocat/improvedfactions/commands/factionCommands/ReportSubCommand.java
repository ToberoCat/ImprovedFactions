package io.github.toberocat.improvedfactions.commands.factionCommands;

import io.github.toberocat.improvedfactions.ImprovedFactionsMain;
import io.github.toberocat.improvedfactions.commands.subCommands.SubCommand;
import io.github.toberocat.improvedfactions.factions.Faction;
import io.github.toberocat.improvedfactions.language.Language;
import io.github.toberocat.improvedfactions.reports.Report;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ReportSubCommand extends SubCommand {
    public ReportSubCommand() {
        super("report", "");
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        if (args.length <= 1) {
            CommandExecuteError(CommandExecuteError.NotEnoughArgs, player);
            return;
        }

        String reason = Language.format(Arrays.stream(args).skip(1).collect(Collectors.joining(" ")));
        ImprovedFactionsMain.REPORTS.add(new Report(player.getUniqueId(), reason, args[0]));

        player.sendMessage(Language.getPrefix() + "Reported " + args[0] + " because of " + reason);
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        LinkedList<String> str = new LinkedList<>();
        if (args.length <= 1) {
            for (Faction faction : Faction.getFACTIONS()) {
                str.add(faction.getRegistryName());
            }
        } else {
            str.add("Reason");
        }
        return str;
    }
}
