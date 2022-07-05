package io.github.toberocat.improvedfactions.commands;

import io.github.toberocat.improvedfactions.commands.factionCommands.JoinPrivateFactionSubCommand;
import io.github.toberocat.improvedfactions.commands.subCommands.SubCommand;
import io.github.toberocat.improvedfactions.factions.Faction;
import io.github.toberocat.improvedfactions.factions.FactionUtils;
import io.github.toberocat.improvedfactions.language.Language;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class FJoin implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender player, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length != 2) return false;

        OfflinePlayer offP = Bukkit.getOfflinePlayer(args[0]);
        if (offP == null) {
            player.sendMessage(Language.getPrefix() +
                    "Player isn't found");
            return false;
        }

        if (!offP.isOnline()) {
            player.sendMessage(Language.getPrefix() + "Player is offline. Can't do this with a offline player");
            return false;
        }

        Player onP = offP.getPlayer();

        Faction faction = FactionUtils.getFaction(onP);
        if (faction != null) {
            faction.Leave(onP);
        }

        JoinPrivateFactionSubCommand.JoinPrivate(onP, args[1]);
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
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
