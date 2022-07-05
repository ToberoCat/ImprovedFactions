package io.github.toberocat.core.commands.zones;

import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import org.bukkit.entity.Player;

import java.util.List;

public class ZoneSubCommand extends SubCommand {
    public ZoneSubCommand() {
        super("zones", "command.zones.description", true);
        subCommands.add(new SafeZoneSubCommand());
        subCommands.add(new UnclaimSubCommand());
        subCommands.add(new WarZoneSubCommand());
        subCommands.add(new UnclaimableZoneSubCommand());
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setUseWhenFrozen(true);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {

    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }
}
