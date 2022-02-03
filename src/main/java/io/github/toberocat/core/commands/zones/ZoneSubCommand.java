package io.github.toberocat.core.commands.zones;

import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.language.LangMessage;
import org.bukkit.entity.Player;

import java.util.List;

public class ZoneSubCommand extends SubCommand  {
    public ZoneSubCommand() {
        super("zones", LangMessage.COMMAND_ZONES_DESCRIPTION, true);
        subCommands.add(new SafeZoneSubCommand());
        subCommands.add(new UnclaimSubCommand());
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {

    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }
}
