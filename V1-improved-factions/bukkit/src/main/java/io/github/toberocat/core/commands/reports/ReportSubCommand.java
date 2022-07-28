package io.github.toberocat.core.commands.reports;

import io.github.toberocat.core.factions.handler.FactionHandler;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class ReportSubCommand extends SubCommand {
    public ReportSubCommand() {
        super("report", "command.report.description", false);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings();
    }

    @Override
    protected void commandExecute(Player player, String[] args) {
        if (args.length <= 1) {
            sendCommandExecuteError(CommandExecuteError.ToLessArgs, player);
            return;
        }

        try {
            FactionHandler.getFaction(args[0])
                    .getReports()
                    .addReport(player,
                            String.join(" ", Arrays.copyOfRange(args, 1, args.length)));
        } catch (FactionNotInStorage e) {
            Language.sendMessage("command.report.faction-not-found", player);
        }
    }

    @Override
    protected List<String> commandTab(Player player, String[] args) {
        if (args.length <= 1) return FactionHandler.getAllFactions().toList();
        return List.of("<reason>");
    }
}
