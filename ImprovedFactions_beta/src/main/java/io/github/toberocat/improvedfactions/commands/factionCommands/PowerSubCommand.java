package io.github.toberocat.improvedfactions.commands.factionCommands;

import io.github.toberocat.improvedfactions.commands.subCommands.SubCommand;
import io.github.toberocat.improvedfactions.commands.subCommands.SubCommandSettings;
import io.github.toberocat.improvedfactions.factions.Faction;
import io.github.toberocat.improvedfactions.factions.FactionUtils;
import io.github.toberocat.improvedfactions.language.Language;
import org.bukkit.entity.Player;

import java.util.List;

public class PowerSubCommand extends SubCommand {
    public PowerSubCommand() {
        super("power", "");
    }

    public SubCommandSettings getSettings() {
        return super.getSettings().setNeedsFaction(SubCommandSettings.NYI.Yes);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        Faction faction = null;
        if (args.length == 0) {
            faction = FactionUtils.getFaction(player);
        } else {
            faction = FactionUtils.getFactionByRegistry(args[0]);
        }

        player.sendMessage(Language.getPrefix() + "§e"+faction.getDisplayName()+"'s current power: " +
                faction.getPowerManager().getPower() + " / " +
                faction.getPowerManager().getMaxPower());
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }
}
