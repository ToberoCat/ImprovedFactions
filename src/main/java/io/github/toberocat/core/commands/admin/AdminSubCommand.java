package io.github.toberocat.core.commands.admin;

import io.github.toberocat.core.commands.factions.JoinFactionSubCommand;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.language.LangMessage;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class AdminSubCommand extends SubCommand {
    public AdminSubCommand() {
        super("admin", LangMessage.COMMAND_ADMIN_DESCRIPTION, true);
        subCommands.addAll(
                Set.of(new AdminHardResetSubCommand(),
                        new AdminDisbandSubCommand(),
                        new AdminGetPlayerFactionSubCommand(),
                        new AdminIsPlayerInFactionSubCommand(),
                        new AdminRegenerateSubCommand(),
                        new AdminPermanentSubCommand(),
                        new AdminFreezeSubCommand(),
                        new JoinFactionSubCommand(),
                        new JoinPrivateFactionSubCommand()));

    }

    @Override
    protected void CommandExecute(Player player, String[] args) {

    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }
}
