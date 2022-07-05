package io.github.toberocat.improvedfactions.commands.factionCommands;

import io.github.toberocat.improvedfactions.commands.factionCommands.adminSubCommands.*;
import io.github.toberocat.improvedfactions.commands.subCommands.SubCommand;
import io.github.toberocat.improvedfactions.language.LangMessage;
import io.github.toberocat.improvedfactions.language.Language;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AdminSubCommand extends SubCommand {

    public static List<SubCommand> subCommands = new ArrayList<>();;

    public AdminSubCommand() {
        super("admin", LangMessage.ADMIN_DESCRIPTION);
        subCommands.add(new DisbandFactionCommand());
        subCommands.add(new JoinPrivateAdminSubCommand());
        subCommands.add(new GPowerSubCommand());
        subCommands.add(new UUnclaimSubCommand());
        subCommands.add(new SafezoneSubCommand());
        subCommands.add(new FonlineSubCommand());
        subCommands.add(new ViewReportsSubCommand());
        subCommands.add(new RenameSubCommand());
        subCommands.add(new ForceJoin());
        subCommands.add(new RemoveReport());
        subCommands.add(new ClearReports());
        subCommands.add(new FrozeAdminCommand());
        subCommands.add(new Permanent());
        subCommands.add(new ByPassSubCommand());

    }


    @Override
    protected void CommandExecute(Player player, String[] args) {
        if(!SubCommand.CallSubCommands(subCommands, player, args)) {
            Language.sendMessage(LangMessage.THIS_COMMAND_DOES_NOT_EXIST, player);
        }
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return SubCommand.CallSubCommandsTab(subCommands, player, args);
    }
}
