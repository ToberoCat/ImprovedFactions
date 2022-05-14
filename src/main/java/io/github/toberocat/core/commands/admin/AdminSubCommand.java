package io.github.toberocat.core.commands.admin;

import io.github.toberocat.core.commands.admin.maxpower.AdminMaxPowerCommand;
import io.github.toberocat.core.commands.admin.power.AdminPowerCommand;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import org.bukkit.entity.Player;

import java.util.List;

public class AdminSubCommand extends SubCommand {
    public AdminSubCommand() {
        super("admin", "command.admin.description", true);
        subCommands.add(new AdminHardResetSubCommand());
        subCommands.add(new AdminDisbandSubCommand());
        subCommands.add(new AdminGetPlayerFactionSubCommand());
        subCommands.add(new AdminIsPlayerInFactionSubCommand());
        subCommands.add(new AdminRegenerateSubCommand());
        subCommands.add(new AdminPermanentSubCommand());
        subCommands.add(new AdminFreezeSubCommand());
        subCommands.add(new JoinPrivateFactionSubCommand());
        subCommands.add(new AdminTimeoutSubCommand());
        subCommands.add(new AdminRemoveTimeoutSubCommand());
        subCommands.add(new AdminBypassSubCommand());
        subCommands.add(new AdminPowerCommand());
        subCommands.add(new AdminMaxPowerCommand());
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
