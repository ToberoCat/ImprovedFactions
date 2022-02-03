package io.github.toberocat.core.commands;

import io.github.toberocat.core.commands.admin.AdminSubCommand;
import io.github.toberocat.core.commands.config.ConfigSubCommand;
import io.github.toberocat.core.commands.extension.ExtensionSubCommand;
import io.github.toberocat.core.commands.factions.CreateFactionSubCommand;
import io.github.toberocat.core.commands.factions.DeleteFactionSubCommand;
import io.github.toberocat.core.commands.factions.claim.ClaimSubCommand;
import io.github.toberocat.core.commands.factions.relation.RelationSubCommand;
import io.github.toberocat.core.commands.plugin.PluginSubCommand;
import io.github.toberocat.core.commands.settings.SettingsSubCommand;
import io.github.toberocat.core.commands.zones.ZoneSubCommand;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.language.LangMessage;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FactionCommand implements TabExecutor {

    public static List<SubCommand> subCommands = new ArrayList<>();

    public FactionCommand() {
        subCommands.add(new ConfigSubCommand());
        subCommands.add(new PluginSubCommand());
        subCommands.add(new CreateFactionSubCommand());
        subCommands.add(new DeleteFactionSubCommand());
        subCommands.add(new ZoneSubCommand());
        subCommands.add(new HelpSubCommand());
        subCommands.add(new AdminSubCommand());
        subCommands.add(new SettingsSubCommand());
        subCommands.add(new ClaimSubCommand());
        subCommands.add(new RelationSubCommand());
        subCommands.add(new ExtensionSubCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //Player is null if commandblock or console
        Player player = null;

        if (sender instanceof Player) {
            player = (Player) sender;
        }

        if (args.length == 0 && player != null) {
            HelpSubCommand.Help(player);
            return true;
        }

        Player finalPlayer = player;
        SubCommand.CallSubCommands("", subCommands, player, args).setFinishCallback((result) -> {
            if (!result) Language.sendMessage(LangMessage.THIS_COMMAND_DOES_NOT_EXIST, finalPlayer);

        });
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }

        List<String> arguments = SubCommand.CallSubCommandsTab(FactionCommand.subCommands, player, args);

        if (arguments == null) return null;

        List<String> results = new ArrayList<>();
        for (String arg : args) {
            for (String a : arguments) {
                if (a.toLowerCase().startsWith(arg.toLowerCase())) {
                    results.add(a);
                }
            }
        }

        return results;
    }
}
