package io.github.toberocat.improvedfactions.commands.factionCommands.adminSubCommands;

import io.github.toberocat.improvedfactions.commands.subCommands.SubCommand;
import io.github.toberocat.improvedfactions.factions.Faction;
import io.github.toberocat.improvedfactions.factions.FactionUtils;
import io.github.toberocat.improvedfactions.gui.FlagUtils;
import io.github.toberocat.improvedfactions.language.LangMessage;
import io.github.toberocat.improvedfactions.language.Language;
import io.github.toberocat.improvedfactions.ranks.AdminRank;
import io.github.toberocat.improvedfactions.ranks.Rank;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class JoinPrivateAdminSubCommand extends SubCommand {
    public JoinPrivateAdminSubCommand() {
        super("sJoin", LangMessage.ADMIN_JOIN_DESCRIPTION);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        if (1 == args.length) {
            Faction faction = FactionUtils.getFaction(player);
            if (faction != null) {
                faction.Leave(player);
                player.sendMessage(Language.getPrefix() + "§cYou left your old faction");
            }

             faction = FactionUtils.getFactionByRegistry(args[0]);
                if (faction == null) {
                    player.sendMessage(Language.getPrefix() + "§cCouldn't find faction to delete");
                    return;
                }

                faction.JoinSilent(player, Rank.fromString(AdminRank.registry));
                player.sendMessage(Language.getPrefix() + "§fYou successfully joined " + faction.getDisplayName() + " silently");
        } else {
            CommandExecuteError(CommandExecuteError.NotEnoughArgs, player);
        }
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        List<String> arguments = new ArrayList<>();
        if (args.length != 1) return arguments;
        for (Faction faction : Faction.getFACTIONS()) {
            arguments.add(faction.getRegistryName());
        }
        return arguments;
    }
}
