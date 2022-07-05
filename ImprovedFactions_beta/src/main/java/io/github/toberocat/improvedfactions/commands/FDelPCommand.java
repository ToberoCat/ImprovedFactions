package io.github.toberocat.improvedfactions.commands;

import io.github.toberocat.improvedfactions.factions.Faction;
import io.github.toberocat.improvedfactions.factions.FactionMember;
import io.github.toberocat.improvedfactions.factions.FactionUtils;
import io.github.toberocat.improvedfactions.gui.FlagUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FDelPCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 0) {
            commandSender.sendMessage("Please add a faction name to delete");
            return false;
        }

        Faction faction = FactionUtils.getFaction(Bukkit.getOfflinePlayer(args[0]).getUniqueId());

        if (faction == null) {
            commandSender.sendMessage("Faction does not exist");
            return false;
        }

        faction.DeleteFaction();
        commandSender.sendMessage("Successfully deleted the faction");
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> arguments = new ArrayList<>();
        if (args.length != 1) return arguments;
        for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            arguments.add(player.getName());
        }
        return arguments;
    }
}