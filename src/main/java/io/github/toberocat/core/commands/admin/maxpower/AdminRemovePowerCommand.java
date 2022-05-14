package io.github.toberocat.core.commands.admin.maxpower;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.FactionUtility;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public class AdminRemovePowerCommand extends SubCommand {
    public AdminRemovePowerCommand() {
        super("remove", "admin.maxpower.remove", "command.admin.maxpower.remove.description", false);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setArgLength(2);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        if (!Utility.isNumber(args[1])) return;
        int amount = Integer.parseInt(args[1]);

        Faction faction = FactionUtility.getFactionByRegistry(args[0]);

        faction.getPowerManager().setMaxPower(faction.getPowerManager().getMaxPower() - amount);
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        LinkedList<String> tab = new LinkedList<>();
        if (args.length <= 1) tab.addAll(FactionUtility.getAllFactions());
        else tab.add("<amount>");

        return tab;
    }
}
