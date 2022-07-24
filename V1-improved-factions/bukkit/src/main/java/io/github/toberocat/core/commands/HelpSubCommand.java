package io.github.toberocat.core.commands;

import io.github.toberocat.core.utility.async.AsyncTask;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.messages.PlayerMessageBuilder;
import org.bukkit.entity.Player;

import java.util.List;

public class HelpSubCommand extends SubCommand {
    public HelpSubCommand() {
        super("help", "command.help.description", false);
    }

    public static void Help(Player player) {
        AsyncTask.run(() -> {
            new PlayerMessageBuilder("&7Usage:&f Hover%Now your" +
                    " hovering;{HOVER}% to see the description, click to execute.")
                    .send(player);

            for (SubCommand commandSub : FactionCommand.subCommands) {
                new PlayerMessageBuilder("&e&l" + commandSub.getSubCommand() + "%" +
                        Language.getMessage(commandSub.getDescription(), player) + ";{HOVER}{CLICK(0)}% &r&7- &8/f " + commandSub.getSubCommand(),
                        "/f " + commandSub.getSubCommand()).send(player);
            }
        });
    }

    @Override
    protected void commandExecute(Player player, String[] args) {
        Help(player);
    }

    @Override
    protected List<String> commandTab(Player player, String[] args) {
        return null;
    }
}
