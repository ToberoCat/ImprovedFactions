package io.github.toberocat.improvedfactions.commands.factionCommands.adminSubCommands;

import io.github.toberocat.improvedfactions.commands.factionCommands.JoinPrivateFactionSubCommand;
import io.github.toberocat.improvedfactions.commands.subCommands.SubCommand;
import io.github.toberocat.improvedfactions.factions.Faction;
import io.github.toberocat.improvedfactions.factions.FactionUtils;
import io.github.toberocat.improvedfactions.language.Language;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ForceJoin extends SubCommand {
    public ForceJoin() {
        super("forcejoin", "");
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        if (args.length != 2) return;

        OfflinePlayer offP = Bukkit.getOfflinePlayer(args[0]);
        if (offP == null) {
            CommandExecuteError(CommandExecuteError.PlayerNotFound, player);
            return;
        }

        if (!offP.isOnline()) {
            player.sendMessage(Language.getPrefix() + "§Play is offline. Can't do this with a offline player");
            return;
        }

        Player onP = offP.getPlayer();

        Faction faction = FactionUtils.getFaction(onP);
        if (faction != null) {
            faction.Leave(onP);
        }

        JoinPrivateFactionSubCommand.JoinPrivate(onP, args[1]);
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        LinkedList<String> str = new LinkedList<>();

        if (args.length == 2) {
            for (Faction f : Faction.getFACTIONS()) {
                str.add(f.getRegistryName());
            }
        } else {
            str.addAll(Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()));
        }

        return str;
    }
}
