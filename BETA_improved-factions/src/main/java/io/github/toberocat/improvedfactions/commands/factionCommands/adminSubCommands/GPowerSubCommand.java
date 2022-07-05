package io.github.toberocat.improvedfactions.commands.factionCommands.adminSubCommands;

import io.github.toberocat.improvedfactions.commands.subCommands.SubCommand;
import io.github.toberocat.improvedfactions.factions.Faction;
import io.github.toberocat.improvedfactions.factions.FactionUtils;
import io.github.toberocat.improvedfactions.language.LangMessage;
import io.github.toberocat.improvedfactions.language.Language;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GPowerSubCommand extends SubCommand  {
    public GPowerSubCommand() {
        super("gpower", LangMessage.ADMIN_GPOWER_DESCRIPTION);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        if (args.length == 2) {
            Faction faction = FactionUtils.getFactionByRegistry(args[0]);

            if (faction == null) {
                player.sendMessage(Language.getPrefix() + "§cCan't find the faction");
                return;
            }

            int power = 0;
            try {
                power = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                player.sendMessage(Language.getPrefix() + "§cPower is no number");
                return;
            }

            faction.getPowerManager().setPower(faction.getPowerManager().getPower() + power);
            player.sendMessage(Language.getPrefix() + "§f Power has been set to §6" + faction.getPowerManager().getPower());
        }
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        List<String> str = new ArrayList<>();
        if (args.length == 1) {
            for (Faction faction : Faction.getFACTIONS()) {
                str.add(ChatColor.stripColor(faction.getDisplayName()));
            }
        }

        return str;
    }
}
