package io.github.toberocat.improvedfactions.commands.factionCommands.ranksCommands;

import io.github.toberocat.improvedfactions.ImprovedFactionsMain;
import io.github.toberocat.improvedfactions.commands.subCommands.SubCommand;
import io.github.toberocat.improvedfactions.data.PlayerData;
import io.github.toberocat.improvedfactions.factions.FactionMember;
import io.github.toberocat.improvedfactions.language.LangMessage;
import io.github.toberocat.improvedfactions.language.Language;
import io.github.toberocat.improvedfactions.language.Parseable;
import io.github.toberocat.improvedfactions.ranks.OwnerRank;
import io.github.toberocat.improvedfactions.ranks.Rank;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SetUserRankSubCommand extends SubCommand {

    public SetUserRankSubCommand() {
        super("set", "rank.set", LangMessage.RANK_SET);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        PlayerData data = ImprovedFactionsMain.playerData.get(player.getUniqueId());
        if (args.length == 2) {
            if (Bukkit.getPlayer(args[0]) != null) {
                if (Rank.fromString(args[1]) != null && !args[0].equals(OwnerRank.registry)) {
                    Player p = Bukkit.getPlayer(args[0]);
                    data.playerFaction.SetRank(p, Rank.fromString(args[1]));
                    Language.sendMessage(LangMessage.RANK_SET_SUCCESS, player, new Parseable("{player}", p.getDisplayName()),
                            new Parseable("{rank}", Rank.fromString(args[1]).getDisplayName()));
                } else {
                    player.sendMessage(Language.getPrefix() + "§cCannot find rank");
                }
            } else {
                CommandExecuteError(CommandExecuteError.PlayerNotFound,player);
            }
        } else {
            CommandExecuteError(CommandExecuteError.NotEnoughArgs,player);
        }
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        List<String> arguments = new ArrayList<String>();
        if (args.length == 1) {
            PlayerData data = ImprovedFactionsMain.playerData.get(player.getUniqueId());
            for (FactionMember uuid : data.playerFaction.getMembers()) {
                if (uuid != null) {
                    OfflinePlayer ofPlayer = Bukkit.getPlayer(uuid.getUuid());
                    if (ofPlayer == null) continue;
                    arguments.add(ofPlayer.getName());
                }
            }
        } else if (args.length == 2) {
            for (Rank rank : Rank.ranks) {
                if (!rank.getRegistryName().equals(OwnerRank.registry)) {
                    arguments.add(rank.toString());
                }
            }
        }
        return arguments;
    }
}
