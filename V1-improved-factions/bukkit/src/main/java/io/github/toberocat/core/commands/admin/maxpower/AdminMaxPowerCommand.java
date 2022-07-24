package io.github.toberocat.core.commands.admin.maxpower;

import io.github.toberocat.core.utility.command.SubCommand;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

public class AdminMaxPowerCommand extends SubCommand {
    public AdminMaxPowerCommand() {
        super("maxpower", "admin.maxpower", "command.admin.maxpower.description", true);
        subCommands.addAll(Set.of(
                new AdminGivePowerCommand(),
                new AdminRemovePowerCommand(),
                new AdminSetPowerCommand()
        ));
    }

    @Override
    protected void commandExecute(Player player, String[] args) {

    }

    @Override
    protected List<String> commandTab(Player player, String[] args) {
        return null;
    }
}
