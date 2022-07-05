package io.github.toberocat.improvedfactions.commands;

import io.github.toberocat.improvedfactions.factions.Faction;
import io.github.toberocat.improvedfactions.factions.FactionMember;
import io.github.toberocat.improvedfactions.factions.FactionUtils;
import io.github.toberocat.improvedfactions.gui.FlagUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FDelCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 0) {
            commandSender.sendMessage("Please add a faction name to delete");
            return false;
        }

        Faction faction = FactionUtils.getFactionByRegistry(args[0]);

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
        for (Faction faction : Faction.getFACTIONS()) {
            arguments.add(ChatColor.stripColor(faction.getDisplayName()));
        }
        return arguments;
    }
}
