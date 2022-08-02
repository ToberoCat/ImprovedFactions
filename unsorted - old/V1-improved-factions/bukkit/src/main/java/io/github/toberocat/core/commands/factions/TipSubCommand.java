package io.github.toberocat.core.commands.factions;

import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.tips.TipOfTheDay;
import org.bukkit.entity.Player;

import java.util.List;

public class TipSubCommand extends SubCommand {
    public TipSubCommand() {
        super("tip", "command.tipOfTheDay.description", false);
    }

    @Override
    protected void commandExecute(Player player, String[] args) {
        TipOfTheDay.sendTipOfTheDay(player);
    }

    @Override
    protected List<String> commandTab(Player player, String[] args) {
        return null;
    }
}
