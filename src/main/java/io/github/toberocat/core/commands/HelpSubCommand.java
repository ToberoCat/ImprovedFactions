package io.github.toberocat.core.commands;

import io.github.toberocat.core.utility.async.AsyncCore;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.language.LangMessage;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.messages.PlayerMessageBuilder;
import org.bukkit.entity.Player;

import java.util.List;

public class HelpSubCommand extends SubCommand {
    public HelpSubCommand() {
        super("help", LangMessage.COMMAND_HELP_DESCRIPTION, false);
    }

    public static void Help(Player player) {
        AsyncCore.Run(() -> {
            new PlayerMessageBuilder("&7Usage:&f Hover%Now your" +
                    " hovering;{HOVER}% to see the description, click to execute.")
                    .send(player);

            for (SubCommand commandSub : FactionCommand.subCommands) {
                new PlayerMessageBuilder("&e&l"+commandSub.getSubCommand() + "%" +
                        Language.getMessage(commandSub.getDescription(), player) + ";{HOVER}{CLICK(0)}% &r&7- &8/f " + commandSub.getSubCommand(),
                        "/f " + commandSub.getSubCommand()).send(player);
            }
        });
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        Help(player);
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }
}
