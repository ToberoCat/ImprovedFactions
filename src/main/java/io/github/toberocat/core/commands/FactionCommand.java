package io.github.toberocat.core.commands;

import io.github.toberocat.core.commands.admin.AdminSubCommand;
import io.github.toberocat.core.commands.config.ConfigSubCommand;
import io.github.toberocat.core.commands.extension.ExtensionSubCommand;
import io.github.toberocat.core.commands.factions.*;
import io.github.toberocat.core.commands.factions.claim.ClaimSubCommand;
import io.github.toberocat.core.commands.factions.relation.RelationSubCommand;
import io.github.toberocat.core.commands.plugin.PluginSubCommand;
import io.github.toberocat.core.commands.settings.FactionSettingsSubCommand;
import io.github.toberocat.core.commands.settings.PlayerSettingsSubCommand;
import io.github.toberocat.core.commands.zones.UnclaimSubCommand;
import io.github.toberocat.core.commands.zones.ZoneSubCommand;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class FactionCommand implements TabExecutor {

    public static LinkedHashSet<SubCommand> subCommands = new LinkedHashSet<>();

    public FactionCommand() {
        subCommands.addAll(Set.of(
                new ConfigSubCommand(),
                new PluginSubCommand(),
                new CreateFactionSubCommand(),
                new DeleteFactionSubCommand(),
                new ZoneSubCommand(),
                new HelpSubCommand(),
                new AdminSubCommand(),
                new FactionSettingsSubCommand(),
                new PlayerSettingsSubCommand(),
                new ClaimSubCommand(),
                new RelationSubCommand(),
                new ExtensionSubCommand(),
                new LeaveFactionSubCommand(),
                new WhoSubCommand(),
                new BanSubCommand(),
                new UnBanSubCommand(),
                new KickSubCommand(),
                new OnlineSubCommand(),
                new JoinFactionSubCommand(),
                new MembersSubCommand(),
                new InviteSubCommand(),
                new InviteAcceptSubCommand(),
                new UnclaimSubCommand(),
                new FactionMapSubCommand()
        ));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
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
        SubCommand.CallSubCommands("", subCommands, player, args).then((result) -> {
            if (!result) Language.sendMessage("command.not-exist", finalPlayer);

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
