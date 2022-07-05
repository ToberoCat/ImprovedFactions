package io.github.toberocat.improvedfactions.commands;

import io.github.toberocat.improvedfactions.commands.factionCommands.*;
import io.github.toberocat.improvedfactions.commands.factionCommands.adminSubCommands.WarnSubCommand;
import io.github.toberocat.improvedfactions.commands.factionCommands.relations.*;
import io.github.toberocat.improvedfactions.language.LangMessage;
import io.github.toberocat.improvedfactions.language.Language;
import io.github.toberocat.improvedfactions.ImprovedFactionsMain;
import io.github.toberocat.improvedfactions.commands.subCommands.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FactionCommand implements CommandExecutor {

    public FactionCommand() {
        subCommands.add(new HelpSubCommand());
        subCommands.add(new CreateSubCommand());
        subCommands.add(new LeaveSubCommand());
        subCommands.add(new JoinSubCommand());
        subCommands.add(new SaveSubCommand());
        subCommands.add(new ExtensionSubCommand());
        subCommands.add(new DeleteSubCommand());
        subCommands.add(new ClaimChunkSubCommand());
        subCommands.add(new UnClaimChunkCommands());
        subCommands.add(new VersionSubCommand());
        subCommands.add(new SettingsSubCommand());
        subCommands.add(new JoinPrivateFactionSubCommand());
        subCommands.add(new InviteSubCommand());
        subCommands.add(new RankSubCommand());
        subCommands.add(new KickSubCommand());
        subCommands.add(new MapSubCommand());
        subCommands.add(new DescriptionSubCommand());
        subCommands.add(new BanSubCommand());
        subCommands.add(new UnbanSubCommand());
        subCommands.add(new ReloadConfigSubCommand());
        subCommands.add(new ListBannedSubCommand());
        subCommands.add(new RulesSubCommand());
        subCommands.add(new SetRulesSubCommand());
        subCommands.add(new AdminSubCommand());
        subCommands.add(new WhoSubCommand());
        subCommands.add(new WarnSubCommand());

        subCommands.add(new AllySubCommand());
        subCommands.add(new AllyAcceptSubCommand());
        subCommands.add(new AllyDenySubCommand());
        subCommands.add(new WarSubCommand());
        subCommands.add(new NeutralSubCommand());
        subCommands.add(new PowerSubCommand());
        subCommands.add(new ClaimPower());
        subCommands.add(new TutorialCommand());
        subCommands.add(new InviteAccept());
        subCommands.add(new ReportSubCommand());
    }

    public static List<SubCommand> subCommands = new ArrayList<SubCommand>();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            //Player
            Player player = (Player) sender;
            if (!player.isOp() && !ImprovedFactionsMain.getPlugin().getConfig().getList("general.worlds")
                    .contains(player.getLocation().getWorld().getName())) {
                Language.sendRawMessage("This world is disabled", player);
                return false;
            }
            if (args.length == 0) {
                Language.sendRawMessage("Can't use without parameters", player);
                return false;
            }


            if(!SubCommand.CallSubCommands(subCommands, player, args)) {
                Language.sendMessage(LangMessage.THIS_COMMAND_DOES_NOT_EXIST, player);
            }
        } else
            ImprovedFactionsMain.getConsoleSender().sendMessage(Language.getPrefix() + "§cYou cannot use this command in the console");
        return false;
    }

    public static void AddSubCommand(SubCommand command) {
        subCommands.add(command);
    }
}
