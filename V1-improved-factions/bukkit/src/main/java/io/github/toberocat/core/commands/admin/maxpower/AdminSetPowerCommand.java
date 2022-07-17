package io.github.toberocat.core.commands.admin.maxpower;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.local.FactionUtility;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public class AdminSetPowerCommand extends SubCommand {

    public AdminSetPowerCommand() {
        super("set", "admin.maxpower.set", "command.admin.maxpower.set.description", false);
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

        faction.getPowerManager().setMaxPower(amount);
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        LinkedList<String> tab = new LinkedList<>();
        if (args.length <= 1) tab.addAll(FactionUtility.getAllFactions());
        else tab.add("<amount>");

        return tab;
    }
}
