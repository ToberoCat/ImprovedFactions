package io.github.toberocat.improvedfactions.commands.factionCommands;

import io.github.toberocat.improvedfactions.commands.factionCommands.ranksCommands.PermissionsRankSubCommand;
import io.github.toberocat.improvedfactions.commands.factionCommands.ranksCommands.SetUserRankSubCommand;
import io.github.toberocat.improvedfactions.commands.subCommands.SubCommand;
import io.github.toberocat.improvedfactions.factions.Faction;
import io.github.toberocat.improvedfactions.factions.FactionUtils;
import io.github.toberocat.improvedfactions.language.LangMessage;
import io.github.toberocat.improvedfactions.language.Language;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RankSubCommand extends SubCommand {

    public static List<SubCommand> subCommands = new ArrayList<>();

    public RankSubCommand() {
        super("rank", LangMessage.RANK_DESCRIPTION);
        subCommands.add(new SetUserRankSubCommand());
        subCommands.add(new PermissionsRankSubCommand());
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        if (FactionUtils.getFaction(player) != null) {
            Faction faction = FactionUtils.getFaction(player);

            if (faction.isFrozen()) {
                CommandExecuteError(CommandExecuteError.Frozen, player);
                return;
            }

            if (FactionUtils.getPlayerRank(faction, player).isAdmin()) {
                if(!SubCommand.CallSubCommands(subCommands, player, args)) {
                    player.sendMessage(Language.getPrefix() + "§cThis command doesn't exist");
                }
            } else {
                CommandExecuteError(CommandExecuteError.NoFactionPermission, player);
            }
        } else {
            CommandExecuteError(CommandExecuteError.NoFaction, player);
        }
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return SubCommand.CallSubCommandsTab(subCommands, player, args);
    }

    @Override
    protected boolean CommandDisplayCondition(Player player, String[] args) {
        boolean result = super.CommandDisplayCondition(player, args);
        Faction faction = FactionUtils.getFaction(player);
        if (faction == null)
            result = false;
        else if (FactionUtils.getPlayerRank(faction, player) != null && !FactionUtils.getPlayerRank(faction, player).isAdmin())
            result = false;
        return result;
    }
}
