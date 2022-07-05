package io.github.toberocat.improvedfactions.tab;

import io.github.toberocat.improvedfactions.ImprovedFactionsMain;
import io.github.toberocat.improvedfactions.commands.FactionCommand;
import io.github.toberocat.improvedfactions.commands.subCommands.SubCommand;
import io.github.toberocat.improvedfactions.language.Language;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FactionCommandTab implements TabCompleter {

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!(sender instanceof Player))
            return null;

        Player player = (Player) sender;

        if (!player.isOp() && !ImprovedFactionsMain.getPlugin().getConfig().getList("general.worlds")
                .contains(player.getLocation().getWorld().getName())) {
            return null;
        }
        //Display results

        List<String> arguments = SubCommand.CallSubCommandsTab(FactionCommand.subCommands, player, args);

        if (arguments == null) return null;

        List<String> results = new ArrayList<String>();
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
