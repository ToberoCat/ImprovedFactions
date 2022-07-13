package io.github.toberocat.core.commands.admin.power;

import io.github.toberocat.core.utility.command.SubCommand;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

public class AdminPowerCommand extends SubCommand {
    public AdminPowerCommand() {
        super("power", "admin.power", "command.admin.power.description", true);
        subCommands.addAll(Set.of(
                new AdminGivePowerCommand(),
                new AdminRemovePowerCommand(),
                new AdminSetPowerCommand()
        ));
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {

    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }
}
